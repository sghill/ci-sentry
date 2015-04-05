package net.sghill.ci.sentry.cli.actions.report;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public class StatsFormatter {
    public static final String ELLIPSIS = "...";
    public static final String HORIZONTAL_BORDER = "+-------------------+----------+----------+-----------+";
    public static final String LINE_ENDING = String.format("%n");

    public String format(SortedMap<String, Stats> args) {
        StringBuilder sb = new StringBuilder(HORIZONTAL_BORDER).append(LINE_ENDING)
                .append("|      Build        | Failures |   Total  | Success % |").append(LINE_ENDING)
                .append(HORIZONTAL_BORDER).append(LINE_ENDING);
        for (Map.Entry<String, Stats> buildStats : args.entrySet()) {
            Stats stats = buildStats.getValue();
            sb.append(String.format("| %-17s | %8d | %8d | %8d%% |%n", ellipsize(buildStats.getKey()), stats.getFailures(), stats.getTotal(), stats.successPercentage()));
        }
        sb.append(HORIZONTAL_BORDER);
        return sb.toString();
    }

    private static String ellipsize(String s) {
        if (s.length() > 17) {
            return s.substring(0, 14).concat(ELLIPSIS);
        }
        return s;
    }

    public static class KeyComparator implements Comparator<String> {
        @Override
        public int compare(String left, String right) {
            return left.compareTo(right);
        }
    }
}
