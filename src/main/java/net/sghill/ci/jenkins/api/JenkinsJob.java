package net.sghill.ci.jenkins.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class JenkinsJob {
    private final String name;
    private final Collection<JenkinsBuild> builds;

    public JenkinsJob(@JsonProperty("name") String name,
                      @JsonProperty("builds") Collection<JenkinsBuild> builds) {
        this.name = name;
        this.builds = builds;
    }
}
