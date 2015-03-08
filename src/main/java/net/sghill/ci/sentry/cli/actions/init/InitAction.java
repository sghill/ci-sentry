package net.sghill.ci.sentry.cli.actions.init;

import net.sourceforge.argparse4j.inf.ArgumentParserException;

import javax.inject.Inject;

public class InitAction implements Runnable {
    public static final String HELP = "create config file and db unless exists";
    private final InitDbAction db;
    private final InitConfigAction config;

    @Inject
    public InitAction(InitDbAction db, InitConfigAction config) {
        this.db = db;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            db.run(null, null, null, null, null);
            config.run(null, null, null, null, null);
        } catch (ArgumentParserException e) {
            throw new RuntimeException(e);
        }
    }
}
