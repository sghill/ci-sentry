package net.sghill.ci.sentry.audit;

import net.sghill.ci.sentry.domain.AuditInfo;

public interface Auditor {
    AuditInfo stamp();
}
