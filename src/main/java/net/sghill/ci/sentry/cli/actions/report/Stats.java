package net.sghill.ci.sentry.cli.actions.report;

import com.google.common.base.MoreObjects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.sghill.ci.sentry.domain.Build;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Stats {
    private final Integer failures;
    private final Integer total;

    public Stats addBuild(Build b) {
        Integer f = MoreObjects.firstNonNull(failures, 0);
        Integer t = MoreObjects.firstNonNull(total, 0) + 1;
        if(b.failed()) {
            return new Stats(f + 1, t);
        }
        return new Stats(f, t);
    }

    public Integer successPercentage() {
        double successes = total - failures;
        double successRate = successes / total;
        return (int)(successRate * 100);
    }
}
