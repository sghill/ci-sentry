package net.sghill.ci.sentry.domain;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import javax.inject.Inject;

public class BuildRepository extends CouchDbRepositorySupport<Build> {
    @Inject
    public BuildRepository(CouchDbConnector db) {
        super(Build.class, db);
    }
}
