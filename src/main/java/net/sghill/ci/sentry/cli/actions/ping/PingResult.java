package net.sghill.ci.sentry.cli.actions.ping;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import retrofit.client.Response;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class PingResult {
    private final URL url;
    private final PingKind kind;
    private final boolean ok;

    public static PingResult fromCiResponse(Response response) {
        return build(response.getUrl(), response.getStatus(), PingKind.CI);
    }

    public static PingResult fromDbResponse(com.squareup.okhttp.Response response) {
        return build(response.request().urlString(), response.code(), PingKind.DB);
    }

    private static PingResult build(String url, Integer status, PingKind kind) {
        try {
            return new PingResult(new URL(url), kind, status == 200);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
