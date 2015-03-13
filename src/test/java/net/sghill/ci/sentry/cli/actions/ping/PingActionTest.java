package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import retrofit.client.Header;
import retrofit.client.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PingActionTest {
    @Mock
    private JenkinsService jenkins;
    @Mock
    private Formatter<PingResult> formatter;
    @Mock
    private Call dbPingCall;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldFormatPingResponseFromJenkinsAndDatabase() throws ArgumentParserException, IOException {
        // Given
        com.squareup.okhttp.Response response = new com.squareup.okhttp.Response.Builder()
                .protocol(Protocol.HTTP_1_1)
                .code(500)
                .request(new Request.Builder().url("http://db/").build())
                .build();
        given(jenkins.ping()).willReturn(new Response("http://jenkins/", 200, "OK", Lists.<Header>newArrayList(), null));
        given(dbPingCall.execute()).willReturn(response);
        PingAction action = new PingAction(jenkins, dbPingCall, formatter);
        Set<PingResult> expectedArguments = Sets.newHashSet(
                new PingResult(new URL("http://jenkins/"), PingKind.CI, true),
                new PingResult(new URL("http://db/"), PingKind.DB, false));

        // When
        action.run();

        // Then
        verify(formatter).format(expectedArguments);
    }
}
