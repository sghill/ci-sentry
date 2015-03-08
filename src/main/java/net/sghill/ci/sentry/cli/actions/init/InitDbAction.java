package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.DatabaseService;
import net.sghill.ci.sentry.config.SentryConfiguration;
import org.slf4j.Logger;

import javax.inject.Inject;

public class InitDbAction implements Runnable {
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
    public void run() {
        InitDbResult result = database.ensureDatabaseExists();
        logger.info(result.getMessageTemplate(), couchDbConfig.getBaseUrl());
    }
}
