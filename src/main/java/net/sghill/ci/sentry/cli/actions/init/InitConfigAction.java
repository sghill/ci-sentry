package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.config.ConfigurationWriter;
import net.sghill.ci.sentry.config.SystemConfiguration;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Map;

public class InitConfigAction implements ArgumentAction {
    public static final String HELP = "place config file in ~/.sentry.yml unless one exists";
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
    public void run(ArgumentParser parser, Argument arg, Map<String, Object> attrs, String flag, Object value) throws ArgumentParserException {
        Path path = systemConfiguration.getUserHome().resolve(".sentry.yml");
        InitConfigResult result = configurationWriter.writeDefaultConfigurationTo(path);
        logger.info(result.getMessage(), path);
    }

    @Override
    public void onAttach(Argument arg) {
    }

    @Override
    public boolean consumeArgument() {
        return false;
    }

}
