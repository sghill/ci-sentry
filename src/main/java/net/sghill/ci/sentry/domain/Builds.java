package net.sghill.ci.sentry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Builds {
    private final Collection<Build> builds;

    public Collection<Build> getDocs() {
        return builds;
    }

    @JsonProperty("all_or_nothing")
    public boolean transactionMode() {
        return true;
    }
}
