package net.sghill.ci.sentry.cli.actions.ping;

import com.google.common.collect.Sets;
import net.sghill.ci.sentry.Database;
import net.sghill.ci.sentry.JenkinsService;
import net.sghill.ci.sentry.cli.Formatter;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentAction;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;

public class PingAction implements ArgumentAction, Runnable {
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
    public void run(ArgumentParser parser, Argument arg, Map<String, Object> attrs, String flag, Object value) throws ArgumentParserException {
        PingResult ci = PingResult.fromCiResponse(jenkins.ping());
        PingResult db = PingResult.fromDbResponse(database.ping());
        LOGGER.info(formatter.format(Sets.newHashSet(ci, db)));
    }

    @Override
    public void onAttach(Argument arg) {
    }

    @Override
    public boolean consumeArgument() {
        return false;
    }

    @Override
    public void run() {
        try {
            run(null, null, null, null, null);
        } catch (ArgumentParserException e) {
            throw new RuntimeException(e);
        }
    }
}
