package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PingResponseFormatterTest {
    @Test
    public void shouldListUrlsAndResults() throws MalformedURLException {
        // Given
        PingResultFormatter formatter = new PingResultFormatter();
        List<PingResult> responses = Lists.newArrayList(
                new PingResult(new URL("http://jenkins-ci/"), PingKind.CI, true),
                new PingResult(new URL("http://jenkins-ci-instance-2/"), PingKind.CI, false),
                new PingResult(new URL("http://my-couch-db:5984/"), PingKind.DB, true),
                new PingResult(new URL("http://my-ridiculously-long-that-is-so-long-url-why-would-anyone-abuse-dns-like-this/"), PingKind.DB, false)
        );

        // When
        String actual = formatter.format(responses);

        // Then
        assertThat(actual).isEqualTo(String.format(
                "    OK: CI (http://jenkins-ci/)%n" +
                "NOT OK: CI (http://jenkins-ci-instance-2/)%n" +
                "    OK: DB (http://my-couch-db:5984/)%n" +
                "NOT OK: DB (http://my-ridiculously-long-that-is-so-long-url-why-would-anyone...)%n"
        ));
    }
}
