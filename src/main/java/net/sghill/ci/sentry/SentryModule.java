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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(SentryModule.class);
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
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        LOGGER.info(message);
                    }
                });
    }

    @Provides
    JenkinsService providesRestAdapter(RestAdapter.Builder builder, SentryConfiguration configuration) {
        SentryConfiguration.Server server = configuration.getServer();
        return builder
                .setEndpoint(server.getBaseUrl())
                .setConverter(new JacksonConverter())
                .setLogLevel(RestAdapter.LogLevel.valueOf(server.getRestLogLevel()))
                .build()
                .create(JenkinsService.class);
    }

    @Provides
    Database providesDatabase(RestAdapter.Builder builder, SentryConfiguration configuration) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        SentryConfiguration.CouchDb couchDb = configuration.getCouchdb();
        return builder
                .setEndpoint(couchDb.getBaseUrl())
                .setConverter(new JacksonConverter(objectMapper))
                .setLogLevel(RestAdapter.LogLevel.valueOf(couchDb.getRestLogLevel()))
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
