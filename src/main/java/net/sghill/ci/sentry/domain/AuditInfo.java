package net.sghill.ci.sentry.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@ToString
@EqualsAndHashCode
public class AuditInfo {
    private final DateTime createdAt;
    private final String createdBy;

    @JsonCreator
    public AuditInfo(@JsonProperty("createdAt") DateTime createdAt, @JsonProperty("createdBy") String createdBy) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
