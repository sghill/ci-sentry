package net.sghill.ci.sentry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.configuration.UrlConfigurationSourceProvider;
import io.dropwizard.jackson.Jackson;
import net.sghill.ci.sentry.cli.ArgParse4JArgParser;
import net.sghill.ci.sentry.cli.ArgParser;
import net.sghill.ci.sentry.cli.Formatter;
import net.sghill.ci.sentry.cli.actions.ping.PingAction;
import net.sghill.ci.sentry.cli.actions.ping.PingResult;
import net.sghill.ci.sentry.cli.actions.ping.PingResultFormatter;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.hibernate.validator.HibernateValidator;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.net.URL;

@Module(library = true,
        injects = {
                Sentry.class
        })
public class SentryModule {
    private final URL configurationFile;

    public SentryModule(URL configurationFile) {
        this.configurationFile = configurationFile;
    }

    @Provides @Singleton
    ValidatorFactory providesValidatorFactory() {
        return Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();
    }

    @Provides
    ConfigurationFactory<SentryConfiguration> providesSentryConfiguration(ValidatorFactory validatorFactory) {
        return new DefaultConfigurationFactoryFactory<SentryConfiguration>().create(
                SentryConfiguration.class,
                validatorFactory.getValidator(),
                Jackson.newObjectMapper(),
                "sentry");
    }

    @Provides @Singleton
    SentryConfiguration providesConfiguration(ConfigurationFactory<SentryConfiguration> configurationFactory) {
        try {
            return configurationFactory.build(new UrlConfigurationSourceProvider(), configurationFile.toString());
        } catch (IOException | ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    RestAdapter.Builder providesRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL);
    }

    @Provides
    JenkinsService providesRestAdapter(RestAdapter.Builder builder, SentryConfiguration configuration) {
        return builder
                .setEndpoint(configuration.getServer().getBaseUrl())
                .setConverter(new JacksonConverter())
                .build()
                .create(JenkinsService.class);
    }

    @Provides
    Database providesDatabase(RestAdapter.Builder builder, SentryConfiguration configuration) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return builder
                .setEndpoint(configuration.getCouchdb().getBaseUrl())
                .setConverter(new JacksonConverter(objectMapper))
                .build()
                .create(Database.class);
    }

    @Provides
    ArgParser<ArgumentParser> providesArgumentParser(Package pkg, PingAction pingAction) {
        return new ArgParse4JArgParser(pkg, pingAction);
    }

    @Provides
    PingAction providesPingAction(JenkinsService jenkins, Database database, Formatter<PingResult> formatter) {
        return new PingAction(jenkins, database, formatter);
    }

    @Provides @Singleton
    Package providesPackage() {
        return getClass().getPackage();
    }

    @Provides @Singleton
    Formatter<PingResult> providesPingResultFormatter() {
        return new PingResultFormatter();
    }
}
