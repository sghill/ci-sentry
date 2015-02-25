package net.sghill.ci.jenkins.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsBuild {
    private final boolean building;
    private final Long duration;
    private final Long number;
    private final net.sghill.ci.jenkins.api.JenkinsBuildResult result;
    private final Long timestamp;

    public JenkinsBuild(@JsonProperty("building") boolean building,
                        @JsonProperty("duration") Long duration,
                        @JsonProperty("number") Long number,
                        @JsonProperty("result") net.sghill.ci.jenkins.api.JenkinsBuildResult result,
                        @JsonProperty("timestamp") Long timestamp) {
        this.building = building;
        this.duration = duration;
        this.number = number;
        this.result = result;
        this.timestamp = timestamp;
    }
}
