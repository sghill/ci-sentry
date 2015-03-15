package net.sghill.ci.sentry.cli.actions.record;

import lombok.RequiredArgsConstructor;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.jenkins.translation.JobTranslator;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.domain.Build;
import org.ektorp.CouchDbConnector;
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
    private final JobTranslator translator;
    private final ForkJoinPool pool;
    private final CouchDbConnector couchDb;

    @Inject
    public RecordBuildsAction(Logger logger, JenkinsService jenkins, JobTranslator translator, ForkJoinPool pool, CouchDbConnector couchDb) {
        this.logger = logger;
        this.jenkins = jenkins;
        this.translator = translator;
        this.pool = pool;
        this.couchDb = couchDb;
    }

    @Override
    public void run() {
        List<JenkinsJob> jobs = jenkins.fetchAllJobs().getJobs();
        logger.info("Found {} jobs", jobs.size());
        pool.invoke(new RecursiveBuildsAction(jobs, logger, translator, couchDb));
    }

    @RequiredArgsConstructor
    public static class RecursiveBuildsAction extends RecursiveAction {
        private final List<JenkinsJob> jobs;
        private final Logger logger;
        private final JobTranslator translator;
        private final CouchDbConnector couch;

        @Override
        protected void compute() {
            int size = jobs.size();
            if (size == 1) {
                JenkinsJob job = jobs.get(0);
                Set<Build> builds = translator.translate(job);
                couch.executeAllOrNothing(builds);
                logger.info("Recorded {} builds for job '{}'", builds.size(), job.getName());
            } else {
                int half = size / 2;
                logger.info("Splitting jobs list in half");
                invokeAll(
                        new RecursiveBuildsAction(jobs.subList(0, half), logger, translator, couch),
                        new RecursiveBuildsAction(jobs.subList(half, size), logger, translator, couch)
                );
            }
        }
    }
}
