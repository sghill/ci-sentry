package net.sghill.ci.sentry.cli;

import net.sghill.ci.sentry.cli.actions.init.InitConfigAction;
import net.sghill.ci.sentry.cli.actions.init.InitConfigResult;
import net.sghill.ci.sentry.config.ConfigurationWriter;
import net.sghill.ci.sentry.config.SystemConfiguration;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class InitConfigActionTest {
    @Mock
    private SystemConfiguration systemConfiguration;
    @Mock
    private ConfigurationWriter configurationWriter;
    @Mock
    private Logger logger;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldPlaceConfigurationInHomeDirectory() throws ArgumentParserException {
        // Given
        InitConfigAction action = new InitConfigAction(logger, systemConfiguration, configurationWriter);
        given(systemConfiguration.getUserHome()).willReturn(Paths.get("/sensible/user/home"));
        Path path = Paths.get("/sensible/user/home/.sentry.yml");
        given(configurationWriter.writeDefaultConfigurationTo(path)).willReturn(InitConfigResult.CREATED);

        // When
        action.run(null, null, null, null, null);

        // Then
        verify(logger).info(InitConfigResult.CREATED.getMessage(), path);
    }
}
