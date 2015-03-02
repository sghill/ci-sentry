package net.sghill.ci.sentry.cli.actions.ping;

import net.sghill.ci.sentry.cli.Formatter;

import java.net.URL;

public class PingResultFormatter implements Formatter<PingResult> {
    @Override
    public String format(Iterable<PingResult> results) {
        StringBuilder sb = new StringBuilder();
        for (PingResult result : results) {
            sb.append(String.format("%6s: %2s (%s)%n", okify(result.isOk()), result.getKind(), ellipsify(result.getUrl())));
        }
        return sb.toString();
    }

    private static String okify(boolean b) {
        return b ? "OK" : "NOT OK";
    }

    private static String ellipsify(URL url) {
        String s = url.toString();
        return s.length() < 68 ? s : s.substring(0, 64) + "...";
    }
}
