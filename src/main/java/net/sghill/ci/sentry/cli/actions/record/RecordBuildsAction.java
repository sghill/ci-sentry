package net.sghill.ci.sentry.cli.actions.record;

import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.jenkins.translation.JobTranslator;
import net.sghill.ci.sentry.Database;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.actions.NoArgAction;
import net.sghill.ci.sentry.domain.Build;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

public class RecordBuildsAction extends NoArgAction implements Runnable {
    public static final String HELP = "save status of all builds";
    private final Logger logger;
    private final JenkinsService jenkins;
    private final Database db;
    private final JobTranslator translator;

    @Inject
    public RecordBuildsAction(Logger logger, JenkinsService jenkins, Database db, JobTranslator translator) {
        this.logger = logger;
        this.jenkins = jenkins;
        this.db = db;
        this.translator = translator;
    }

    @Override
    public void run() {
        Collection<JenkinsJob> jobs = jenkins.fetchAllJobs().getJobs();
        for (JenkinsJob job : jobs) {
            Set<Build> builds = translator.translate(job);
            for (Build build : builds) {
                db.createBuild(build.getId(), build);
                logger.info("created build {}", build.getId());
            }
        }
    }
}
