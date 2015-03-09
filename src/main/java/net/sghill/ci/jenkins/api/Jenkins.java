package net.sghill.ci.jenkins.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jenkins {
    private final List<JenkinsJob> jobs;

    public Jenkins(@JsonProperty("jobs") List<JenkinsJob> jobs) {
        this.jobs = jobs;
    }
}
