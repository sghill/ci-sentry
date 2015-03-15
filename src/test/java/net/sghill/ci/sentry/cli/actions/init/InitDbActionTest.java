package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.config.SentryConfiguration;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.ektorp.CouchDbConnector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class InitDbActionTest {
    @Mock
    private CouchDbConnector db;
    @Mock
    private Logger logger;
    private SentryConfiguration.CouchDb couchDbConfig;
    private InitDbAction action;

    @Before
    public void setUp() {
        initMocks(this);
        couchDbConfig = new SentryConfiguration.CouchDb("http://some-url/");
        action = new InitDbAction(db, couchDbConfig, logger);
    }

    @Test
    public void shouldLogWhenInitDbSuccessful() throws ArgumentParserException {
        // When
        action.run();

        // Then
        verify(db).createDatabaseIfNotExists();
        verify(logger).info(InitDbAction.MESSAGE, couchDbConfig.getBaseUrl());
    }
}
