package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Sets;
import com.squareup.okhttp.Call;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;

public class PingAction implements Runnable {
    public static final String HELP = "ping configured ci and db";
    private final Iterable<JenkinsService> jenkins;
    private final Call dbPing;
    private final Formatter<PingResult> formatter;
    private final Logger logger;

    @Inject
    public PingAction(Iterable<JenkinsService> jenkins, Call dbPing, Formatter<PingResult> formatter, Logger logger) {
        this.jenkins = jenkins;
        this.dbPing = dbPing;
        this.formatter = formatter;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            Set<PingResult> results = Sets.newHashSet(PingResult.fromDbResponse(dbPing.execute()));
            for (JenkinsService j : jenkins) {
                results.add(PingResult.fromCiResponse(j.ping()));
            }
            logger.info(formatter.format(results));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
