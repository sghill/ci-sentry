package net.sghill.ci.sentry.cli.actions.report;

import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.domain.BuildBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatsTest {

    @Test
    public void shouldAlwaysAddToTotal() {
        // Given
        Stats stats = new Stats(0,0);
        Build build = new BuildBuilder().failed().build();

        // When
        Stats updatedStats = stats.addBuild(build);

        // Then
        assertThat(updatedStats).isEqualTo(new Stats(1, 1));
    }

    @Test
    public void shouldOnlyUpdateFailedCountIfFailed() {
        // Given
        Stats stats = new Stats(10,50);
        Build failed = new BuildBuilder().failed().build();
        Build passed = new BuildBuilder().passed().build();

        // When
        Stats updatedStats = stats.addBuild(failed);
        Stats mostRecentStats = updatedStats.addBuild(passed);

        // Then
        assertThat(mostRecentStats).isEqualTo(new Stats(11, 52));
    }
}