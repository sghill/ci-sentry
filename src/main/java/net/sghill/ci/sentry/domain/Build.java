package net.sghill.ci.sentry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;
import org.joda.time.Duration;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"auditInfo"})
@RequiredArgsConstructor
public class Build {
    @JsonProperty("_id")
    private final String id;
    private final String name;
    private final Long run;
    private final Duration duration;
    private final DateTime completedAt;
    private final BuildResult result;
    private final Integer version;
    private final AuditInfo auditInfo;
}
