package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.DatabaseService;
import net.sghill.ci.sentry.config.SentryConfiguration;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Map;

public class InitDbAction implements ArgumentAction {
    public static final String HELP = "create db unless exists";
    private final DatabaseService database;
    private final SentryConfiguration.CouchDb couchDbConfig;
    private final Logger logger;

    @Inject
    public InitDbAction(DatabaseService database, SentryConfiguration.CouchDb couchDbConfig, Logger logger) {
        this.database = database;
        this.couchDbConfig = couchDbConfig;
        this.logger = logger;
    }

    @Override
    public void run(ArgumentParser parser, Argument arg, Map<String, Object> attrs, String flag, Object value) throws ArgumentParserException {
        InitDbResult result = database.ensureDatabaseExists();
        logger.info(result.getMessageTemplate(), couchDbConfig.getBaseUrl());
    }

    @Override
    public void onAttach(Argument arg) {
    }

    @Override
    public boolean consumeArgument() {
        return false;
    }
}
