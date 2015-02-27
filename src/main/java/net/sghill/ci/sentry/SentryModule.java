package net.sghill.ci.sentry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.jackson.Jackson;
import net.sghill.ci.sentry.cli.ArgParse4JArgParser;
import net.sghill.ci.sentry.cli.ArgParser;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import org.hibernate.validator.HibernateValidator;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;

@Module(injects = Sentry.class, library = true)
public class SentryModule {
    private final File configurationFile;

    public SentryModule(File configurationFile) {
        this.configurationFile = configurationFile;
    }

    @Provides
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

    @Provides
    SentryConfiguration providesConfiguration(ConfigurationFactory<SentryConfiguration> configurationFactory) {
        try {
            return configurationFactory.build(configurationFile);
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
    ArgParser<ArgumentParser> providesArgumentParser(Package pkg) {
        return new ArgParse4JArgParser(pkg);
    }

    @Provides
    Package providesPackage() {
        return getClass().getPackage();
    }
}
