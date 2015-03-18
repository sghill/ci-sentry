package net.sghill.ci.sentry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import dagger.Module;
import dagger.Provides;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.configuration.UrlConfigurationSourceProvider;
import io.dropwizard.jackson.Jackson;
import net.sghill.ci.jenkins.translation.JobTranslator;
import net.sghill.ci.sentry.audit.*;
import net.sghill.ci.sentry.cli.ArgParse4JArgParser;
import net.sghill.ci.sentry.cli.Command;
import net.sghill.ci.sentry.cli.Formatter;
import net.sghill.ci.sentry.cli.actions.init.InitAction;
import net.sghill.ci.sentry.cli.actions.init.InitConfigAction;
import net.sghill.ci.sentry.cli.actions.init.InitDbAction;
import net.sghill.ci.sentry.cli.actions.ping.PingAction;
import net.sghill.ci.sentry.cli.actions.ping.PingResult;
import net.sghill.ci.sentry.cli.actions.ping.PingResultFormatter;
import net.sghill.ci.sentry.cli.actions.record.RecordBuildsAction;
import net.sghill.ci.sentry.config.*;
import net.sghill.ci.sentry.domain.BuildRepository;
import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.ObjectMapperFactory;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.impl.jackson.EktorpJacksonModule;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@Module(library = true,
        injects = {
                BuildRepository.class,
                ArgParse4JArgParser.class,
                InitAction.class,
                InitDbAction.class,
                InitConfigAction.class,
                JobTranslator.class,
                PingAction.class,
                PreferentialConfigurationResolver.class,
                RecordBuildsAction.class,
                Sentry.class,
                UserNameAuditor.class,
                UserHomeConfigurationResolver.class,
                YamlConfigurationWriter.class
        })
public class SentryModule {

    @Provides
    ForkJoinPool providesPool(final Logger logger) {
        return new ForkJoinPool(
                Runtime.getRuntime().availableProcessors(),
                ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("exception from fork/join pool", e);
                    }
                },
                true);
    }

    @Provides
    CouchDbConnector providesCouchDbConnector(SentryConfiguration.CouchDb config) {
        try {
            HttpClient client = new StdHttpClient.Builder()
                    .url(config.getBaseUrl())
                    .build();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new CouchDbSerializationModule());
            return new StdCouchDbConnector("sentry", new StdCouchDbInstance(client), new ObjectMapperFactory() {
                @Override
                public ObjectMapper createObjectMapper() {
                    return mapper;
                }

                @Override
                public ObjectMapper createObjectMapper(CouchDbConnector connector) {
                    mapper.registerModule(new EktorpJacksonModule(connector, mapper));
                    return mapper;
                }
            });
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton
    URL providesConfigurationUrl(PreferentialConfigurationResolver resolver) {
        return resolver.resolve();
    }

    @Provides @Singleton
    Clock providesUtcClock() {
        return new UtcClock();
    }

    @Provides @Singleton
    SystemProvider providesSystemProvider() {
        return new JvmSystemProvider();
    }

    @Provides @Singleton
    Auditor providesAuditor(UserNameAuditor auditor) {
        return auditor;
    }

    @Provides @Singleton
    Iterable<ConfigurationResolver> providesOrderedConfigurationResolvers(UserHomeConfigurationResolver userHome) {
        return Lists.newArrayList(userHome, new ClasspathDefaultConfigurationResolver());
    }

    @Provides @Singleton
    ValidatorFactory providesValidatorFactory() {
        return Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();
    }

    @Provides @Singleton
    ObjectMapper providesObjectMapper() {
        return new ObjectMapper(new YAMLFactory());
    }

    @Provides @Singleton
    ConfigurationFactory <SentryConfiguration> providesSentryConfiguration(ValidatorFactory validatorFactory) {
        return new DefaultConfigurationFactoryFactory<SentryConfiguration>().create(
                SentryConfiguration.class,
                validatorFactory.getValidator(),
                Jackson.newObjectMapper(),
                "sentry");
    }


    @Provides @Singleton
    SentryConfiguration providesConfiguration(ConfigurationFactory<SentryConfiguration> configurationFactory, URL configurationFile) {
        try {
            return configurationFactory.build(new UrlConfigurationSourceProvider(), configurationFile.toString());
        } catch (IOException | io.dropwizard.configuration.ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides @Singleton
    OkHttpClient providesOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    RestAdapter.Builder providesRestAdapterBuilder(JacksonConverter converter, OkHttpClient client, final Logger logger) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setConverter(converter)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        logger.info(message);
                    }
                });
    }

    @Provides
    JacksonConverter providesJacksonConverter() {
        return new JacksonConverter(Jackson.newObjectMapper());
    }

    @Provides
    JenkinsService providesRestAdapter(RestAdapter.Builder builder, SentryConfiguration configuration) {
        SentryConfiguration.Server server = configuration.getServer();
        return builder
                .setEndpoint(server.getBaseUrl())
                .setLogLevel(RestAdapter.LogLevel.valueOf(server.getRestLogLevel()))
                .build()
                .create(JenkinsService.class);
    }

    @Provides
    BuildRepository providesBuildRepository(CouchDbConnector connector) {
        return new BuildRepository(connector);
    }

    @Provides
    Map<String, Long> providesMostRecent(BuildRepository repo) {
        return repo.findMostRecent();
    }

    @Provides
    Logger providesLogger() {
        return LoggerFactory.getLogger("Sentry");
    }

    @Provides
    ConfigurationWriter providesConfigurationWriter(YamlConfigurationWriter w) {
        return w;
    }

    @Provides
    FileSystem providesFileSystem() {
        return new StandardFileSystem();
    }

    @Provides @Singleton
    Package providesPackage() {
        return getClass().getPackage();
    }

    @Provides @Singleton
    Formatter<PingResult> providesPingResultFormatter() {
        return new PingResultFormatter();
    }

    @Provides @Singleton
    SystemConfiguration providesSystemConfiguration() {
        return new JavaPropertySystemConfiguration();
    }

    @Provides @Singleton
    SentryConfiguration.CouchDb providesCouchDbConfiguration(SentryConfiguration c) {
        return c.getCouchdb();
    }

    @Provides @Singleton
    Map<Command, Runnable> providesActionFactory(InitAction init, PingAction ping, RecordBuildsAction record) {
        return ImmutableMap.of(
                Command.INIT, init,
                Command.PING, ping,
                Command.RECORD, record
        );
    }

    @Provides
    Call providesPingRequest(SentryConfiguration.CouchDb config, OkHttpClient client) {
        Request req = new Request.Builder()
                .url(config.getBaseUrl())
                .head()
                .build();
        return client.newCall(req);
    }
}
