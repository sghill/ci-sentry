package net.sghill.ci.jenkins.translation;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import net.sghill.ci.jenkins.api.JenkinsBuild;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.sentry.audit.Auditor;
import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.translation.Translator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

public class JobTranslator implements Translator<JenkinsJob, Set<Build>> {
    public static final int VERSION = 1;
    private final Auditor auditor;
    private final Map<String, Long> mostRecentBuild;
    private final Logger logger;

    @Inject
    public JobTranslator(Auditor auditor, Map<String, Long> mostRecentBuild, Logger logger) {
        this.auditor = auditor;
        this.mostRecentBuild = mostRecentBuild;
        this.logger = logger;
    }

    @Override
    public Set<Build> translate(JenkinsJob job) {
        Set<Build> builds = Sets.newHashSet();
        String jobName = job.getName();
        Long mostRecent = MoreObjects.firstNonNull(mostRecentBuild.get(jobName), 0L);
        logger.info("Most recent build recorded for job '{}': {}", jobName, mostRecent);
        for (JenkinsBuild b : job.getBuilds()) {
            if(b.isBuilding() || mostRecent >= b.getNumber()) {
                continue;
            }
            builds.add(new Build(
                    String.format("%s:%d", jobName, b.getNumber()),
                    jobName,
                    b.getNumber(),
                    b.getDuration(),
                    new DateTime(b.getTimestamp(), DateTimeZone.UTC),
                    b.getResult().translate(),
                    VERSION,
                    auditor.stamp()
            ));
        }
        return builds;
    }
}
