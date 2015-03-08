package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.config.ConfigurationWriter;
import net.sghill.ci.sentry.config.SystemConfiguration;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.nio.file.Path;

public class InitConfigAction implements Runnable {
    private final Logger logger;
    private final SystemConfiguration systemConfiguration;
    private final ConfigurationWriter configurationWriter;

    @Inject
    public InitConfigAction(Logger logger, SystemConfiguration systemConfiguration, ConfigurationWriter configurationWriter) {
        this.logger = logger;
        this.systemConfiguration = systemConfiguration;
        this.configurationWriter = configurationWriter;
    }

    @Override
    public void run() {
        Path path = systemConfiguration.getUserHome().resolve(".sentry.yml");
        InitConfigResult result = configurationWriter.writeDefaultConfigurationTo(path);
        logger.info(result.getMessage(), path);
    }
}
