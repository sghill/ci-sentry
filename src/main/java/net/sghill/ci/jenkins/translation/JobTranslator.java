package net.sghill.ci.jenkins.translation;

import com.google.common.collect.Sets;
import net.sghill.ci.jenkins.api.JenkinsBuild;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.sentry.audit.Auditor;
import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.identity.IdentityProvider;
import net.sghill.ci.sentry.translation.Translator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.util.Set;
import java.util.UUID;

public class JobTranslator implements Translator<JenkinsJob, Set<Build>> {
    public static final int VERSION = 1;
    private final IdentityProvider<UUID> identityProvider;
    private final Auditor auditor;

    public JobTranslator(IdentityProvider<UUID> identityProvider, Auditor auditor) {
        this.identityProvider = identityProvider;
        this.auditor = auditor;
    }

    @Override
    public Set<Build> translate(JenkinsJob job) {
        Set<Build> builds = Sets.newHashSet();
        for (JenkinsBuild b : job.getBuilds()) {
            if(b.isBuilding()) {
                continue;
            }
            builds.add(new Build(
                    identityProvider.newId(),
                    job.getName(),
                    String.valueOf(b.getNumber()),
                    new Duration(b.getDuration()),
                    new DateTime(b.getTimestamp(), DateTimeZone.UTC),
                    b.getResult().translate(),
                    VERSION,
                    auditor.stamp()
            ));
        }
        return builds;
    }
}
