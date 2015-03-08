package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Sets;
import net.sghill.ci.sentry.Database;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PingAction implements Runnable {
    public static final String HELP = "ping configured ci and db";
    private static final Logger LOGGER = LoggerFactory.getLogger(PingAction.class);
    private final JenkinsService jenkins;
    private final Database database;
    private final Formatter<PingResult> formatter;

    @Inject
    public PingAction(JenkinsService jenkins, Database database, Formatter<PingResult> formatter) {
        this.jenkins = jenkins;
        this.database = database;
        this.formatter = formatter;
    }

    @Override
    public void run() {
        PingResult ci = PingResult.fromCiResponse(jenkins.ping());
        PingResult db = PingResult.fromDbResponse(database.ping());
        LOGGER.info(formatter.format(Sets.newHashSet(ci, db)));
    }
}
