package net.sghill.ci.sentry.cli.actions.report;

import com.google.common.collect.ImmutableSortedMap;
import org.junit.Test;

import java.util.SortedMap;

import static org.assertj.core.api.Assertions.assertThat;

public class StatsFormatterTest {

    @Test
    public void shouldSortAndAlign() {
        // Given
        SortedMap<String, Stats> stats = ImmutableSortedMap.<String, Stats>naturalOrder()
                .put("build a", new Stats(3, 4))
                .put("build supercalafragalisticexpialadocious", new Stats(71, 1000))
                .put("build beetelguice", new Stats(0, 100))
                .build();
        StatsFormatter formatter = new StatsFormatter();

        // When
        String actual = formatter.format(stats);

        // Then
        assertThat(actual).isEqualTo(String.format(
                "+-------------------+----------+----------+-----------+%n" +
                "|      Build        | Failures |   Total  | Success %% |%n" +
                "+-------------------+----------+----------+-----------+%n" +
                "| build a           |        3 |        4 |       25%% |%n" +
                "| build beetelguice |        0 |      100 |      100%% |%n" +
                "| build supercal... |       71 |     1000 |       92%% |%n" +
                "+-------------------+----------+----------+-----------+"));
    }
}