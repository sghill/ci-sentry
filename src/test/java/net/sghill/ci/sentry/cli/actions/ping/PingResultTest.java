package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Lists;
import org.junit.Test;
import retrofit.client.Header;
import retrofit.client.Response;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class PingResultTest {
    @Test
    public void shouldBuildPingResultFromCiRetrofitResponse() throws MalformedURLException {
        // Given
        Response response = new Response("http://jenkins/", 200, "OK", Lists.<Header>newArrayList(), null);

        // When
        PingResult result = PingResult.fromCiResponse(response);

        // Then
        assertThat(result).isEqualTo(new PingResult(new URL("http://jenkins/"), PingKind.CI, true));
    }

    @Test
    public void shouldBuildPingResultFromDbRetrofitResponse() throws MalformedURLException {
        // Given
        Response response = new Response("http://couchdb/", 500, "INTERNAL SERVER ERROR", Lists.<Header>newArrayList(), null);

        // When
        PingResult result = PingResult.fromDbResponse(response);

        // Then
        assertThat(result).isEqualTo(new PingResult(new URL("http://couchdb/"), PingKind.DB, false));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfUrlMalformed() {
        // Given
        Response response = new Response("abc.123-test_not:URI", 500, "INTERNAL SERVER ERROR", Lists.<Header>newArrayList(), null);

        // When
        PingResult.fromDbResponse(response);

        // Then exception
    }
}
