package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Sets;
import com.squareup.okhttp.Call;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class PingAction implements Runnable {
    public static final String HELP = "ping configured ci and db";
    private static final Logger LOGGER = LoggerFactory.getLogger(PingAction.class);
    private final JenkinsService jenkins;
    private final Call dbPing;
    private final Formatter<PingResult> formatter;

    @Inject
    public PingAction(JenkinsService jenkins, Call dbPing, Formatter<PingResult> formatter) {
        this.jenkins = jenkins;
        this.dbPing = dbPing;
        this.formatter = formatter;
    }

    @Override
    public void run() {
        try {
            PingResult ci = PingResult.fromCiResponse(jenkins.ping());
            PingResult db = PingResult.fromDbResponse(dbPing.execute());
            LOGGER.info(formatter.format(Sets.newHashSet(ci, db)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
