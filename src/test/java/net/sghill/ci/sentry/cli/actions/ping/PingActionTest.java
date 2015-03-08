package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.sghill.ci.sentry.Database;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import retrofit.client.Header;
import retrofit.client.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PingActionTest {
    @Mock
    private JenkinsService jenkins;
    @Mock
    private Database database;
    @Mock
    private Formatter<PingResult> formatter;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldFormatPingResponseFromJenkinsAndDatabase() throws ArgumentParserException, MalformedURLException {
        // Given
        PingAction action = new PingAction(jenkins, database, formatter);
        given(jenkins.ping()).willReturn(new Response("http://jenkins/", 200, "OK", Lists.<Header>newArrayList(), null));
        given(database.ping()).willReturn(new Response("http://db/", 404, "NOT FOUND", Lists.<Header>newArrayList(), null));
        Set<PingResult> expectedArguments = Sets.newHashSet(
                new PingResult(new URL("http://jenkins/"), PingKind.CI, true),
                new PingResult(new URL("http://db/"), PingKind.DB, false));

        // When
        action.run();

        // Then
        verify(formatter).format(expectedArguments);
    }
}
