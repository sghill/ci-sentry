package net.sghill.ci.sentry.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude = {"auditInfo"})
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @JsonCreator
    public Build(@JsonProperty("_id") String id,
                 @JsonProperty("name") String name,
                 @JsonProperty("run") Long run,
                 @JsonProperty("durationInMillis") Long durationInMillis,
                 @JsonProperty("completedAt") DateTime completedAt,
                 @JsonProperty("result") BuildResult result,
                 @JsonProperty("version") Integer version,
                 @JsonProperty("auditInfo") AuditInfo auditInfo) {
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

    public boolean failed() {
        return result == BuildResult.FAILED;
    }
}
