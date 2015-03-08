package net.sghill.ci.sentry.domain;

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
}
