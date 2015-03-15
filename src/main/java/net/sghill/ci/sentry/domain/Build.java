package net.sghill.ci.sentry.domain;

import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.joda.time.Duration;

@Setter
@Getter
@ToString
@EqualsAndHashCode(exclude = {"auditInfo"})
@AllArgsConstructor
public class Build {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_rev")
    private String revision;
    private String name;
    private Long run;
    private Duration duration;
    private DateTime completedAt;
    private BuildResult result;
    private Integer version;
    private AuditInfo auditInfo;
}
