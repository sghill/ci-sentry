package net.sghill.ci.sentry.cli.actions.record;

import lombok.RequiredArgsConstructor;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.jenkins.translation.JobTranslator;
import net.sghill.ci.sentry.Database;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.domain.Builds;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RecordBuildsAction implements Runnable {
    public static final String HELP = "save status of all builds";
    private final Logger logger;
    private final JenkinsService jenkins;
    private final Database db;
    private final JobTranslator translator;
    private final ForkJoinPool pool;

    @Inject
    public RecordBuildsAction(Logger logger, JenkinsService jenkins, Database db, JobTranslator translator, ForkJoinPool pool) {
        this.logger = logger;
        this.jenkins = jenkins;
        this.db = db;
        this.translator = translator;
        this.pool = pool;
    }

    @Override
    public void run() {
        List<JenkinsJob> jobs = jenkins.fetchAllJobs().getJobs();
        logger.info("Found {} jobs", jobs.size());
        pool.invoke(new RecursiveBuildsAction(jobs, logger, db, translator));
    }

    @RequiredArgsConstructor
    public static class RecursiveBuildsAction extends RecursiveAction {
        private final List<JenkinsJob> jobs;
        private final Logger logger;
        private final Database db;
        private final JobTranslator translator;

        @Override
        protected void compute() {
            int size = jobs.size();
            if (size == 1) {
                JenkinsJob job = jobs.get(0);
                Set<Build> builds = translator.translate(job);
                db.createBuilds(new Builds(builds));
                logger.info("Recorded {} builds for job '{}'", builds.size(), job.getName());
            } else {
                int half = size / 2;
                logger.info("Splitting jobs list in half");
                invokeAll(
                        new RecursiveBuildsAction(jobs.subList(0, half), logger, db, translator),
                        new RecursiveBuildsAction(jobs.subList(half, size), logger, db, translator)
                );
            }
        }
    }
}
