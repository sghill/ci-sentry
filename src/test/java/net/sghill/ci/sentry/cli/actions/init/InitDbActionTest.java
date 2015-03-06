package net.sghill.ci.sentry.cli.actions.init;

import net.sghill.ci.sentry.DatabaseService;
import net.sghill.ci.sentry.config.SentryConfiguration;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class InitDbActionTest {
    @Mock
    private DatabaseService databaseService;
    @Mock
    private Logger logger;
    private SentryConfiguration.CouchDb couchDbConfig;
    private InitDbAction action;

    @Before
    public void setUp() {
        initMocks(this);
        couchDbConfig = new SentryConfiguration.CouchDb("http://some-url/", "NONE");
        action = new InitDbAction(databaseService, couchDbConfig, logger);
    }

    @Test
    public void shouldLogWhenInitDbSuccessful() throws ArgumentParserException {
        // Given
        given(databaseService.ensureDatabaseExists()).willReturn(InitDbResult.CREATED);

        // When
        action.run(null, null, null, null, null);

        // Then
        verify(logger).info(InitDbResult.CREATED.getMessageTemplate(), couchDbConfig.getBaseUrl());
    }

    @Test
    public void shouldLogWhenInitDbAlreadyExists() throws ArgumentParserException {
        // Given
        given(databaseService.ensureDatabaseExists()).willReturn(InitDbResult.ALREADY_EXISTED);

        // When
        action.run(null, null, null, null, null);

        // Then
        verify(logger).info(InitDbResult.ALREADY_EXISTED.getMessageTemplate(), couchDbConfig.getBaseUrl());
    }
}
