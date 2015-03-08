package net.sghill.ci.sentry.audit;

import net.sghill.ci.sentry.domain.AuditInfo;

import javax.inject.Inject;

public class UserNameAuditor implements Auditor {
    private final Clock clock;
    private final SystemProvider systemProvider;

    @Inject
    public UserNameAuditor(Clock clock, SystemProvider systemProvider) {
        this.clock = clock;
        this.systemProvider = systemProvider;
    }

    @Override
    public AuditInfo stamp() {
        return new AuditInfo(clock.now(), systemProvider.userName());
    }
}
