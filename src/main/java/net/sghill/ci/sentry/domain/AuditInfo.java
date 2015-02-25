package net.sghill.ci.sentry.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class AuditInfo {
    private final DateTime createdAt;
    private final String createdBy;
}
