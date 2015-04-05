package net.sghill.ci.sentry.domain;

import org.joda.time.DateTime;

public class BuildBuilder extends Builder<Build> {
    private String id = randomString();
    private String name = randomString();
    private Long run = randomLong();
    private Long durationInMillis = randomLong();
    private DateTime completedAt = randomDateTime();
    private BuildResult result = BuildResult.PASSED;
    private Integer version = randomInt();
    private AuditInfo auditInfo = new AuditInfoBuilder().build();

    @Override
    public Build build() {
        return new Build(id, name, run, durationInMillis, completedAt, result, version, auditInfo);
    }

    public BuildBuilder failed() {
        result = BuildResult.FAILED;
        return this;
    }

    public BuildBuilder passed() {
        result = BuildResult.PASSED;
        return this;
    }
}
