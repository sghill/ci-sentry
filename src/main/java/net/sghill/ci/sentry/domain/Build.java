package net.sghill.ci.sentry.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude = {"auditInfo"})
public class Build {
    @JsonProperty("_id")
    private final String id;
    @JsonProperty("_rev")
    private final String revision;
    private final String name;
    private final Long run;
    private final Long durationInMillis;
    private final DateTime completedAt;
    private final BuildResult result;
    private final Integer version;
    private final AuditInfo auditInfo;
    private final Type type = Type.BUILD;

    public Build(String id,
                 String name,
                 Long run,
                 Long durationInMillis,
                 DateTime completedAt,
                 BuildResult result,
                 Integer version,
                 AuditInfo auditInfo) {
        this.id = id;
        this.revision = null;
        this.name = name;
        this.run = run;
        this.durationInMillis = durationInMillis;
        this.completedAt = completedAt;
        this.result = result;
        this.version = version;
        this.auditInfo = auditInfo;
    }
}
