package net.sghill.ci.sentry.domain;

import org.joda.time.DateTime;

public class AuditInfoBuilder extends Builder<AuditInfo> {
    private DateTime createdAt = randomDateTime();
    private String createdBy = randomString();

    @Override
    public AuditInfo build() {
        return new AuditInfo(createdAt, createdBy);
    }
}
