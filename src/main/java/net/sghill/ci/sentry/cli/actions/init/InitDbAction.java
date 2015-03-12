package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.config.SentryConfiguration;
import org.ektorp.CouchDbConnector;
import org.slf4j.Logger;

import javax.inject.Inject;

public class InitDbAction implements Runnable {
    public static final String MESSAGE = "Ensuring database exists at {}";
    private final CouchDbConnector db;
    private final SentryConfiguration.CouchDb couchDbConfig;
    private final Logger logger;

    @Inject
    public InitDbAction(CouchDbConnector db, SentryConfiguration.CouchDb couchDbConfig, Logger logger) {
        this.db = db;
        this.couchDbConfig = couchDbConfig;
        this.logger = logger;
    }

    @Override
    public void run() {
        db.createDatabaseIfNotExists();
        logger.info(MESSAGE, couchDbConfig.getBaseUrl());
    }
}
