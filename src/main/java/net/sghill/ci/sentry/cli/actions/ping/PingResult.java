package net.sghill.ci.sentry.cli.actions.ping;

import lombok.*;
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
        return build(response, PingKind.CI);
    }

    public static PingResult fromDbResponse(Response response) {
        return build(response, PingKind.DB);
    }

    private static PingResult build(Response response, PingKind kind) {
        try {
            return new PingResult(new URL(response.getUrl()), kind, response.getStatus() == 200);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
