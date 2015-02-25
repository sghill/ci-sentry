package net.sghill.ci.sentry.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"id", "auditInfo"})
@RequiredArgsConstructor
public class Build {
    private final UUID id;
    private final String name;
    private final String run;
    private final Duration duration;
    private final DateTime completedAt;
    private final BuildResult result;
    private final Integer version;
    private final AuditInfo auditInfo;
}
