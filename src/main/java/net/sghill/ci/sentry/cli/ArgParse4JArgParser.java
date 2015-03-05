package net.sghill.ci.sentry.cli;

import net.sghill.ci.sentry.cli.actions.init.InitConfigAction;
import net.sghill.ci.sentry.cli.actions.ping.PingAction;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;

import javax.inject.Inject;

public class ArgParse4JArgParser implements ArgParser<ArgumentParser> {
    private final Package pkg;
    private final PingAction pingAction;
    private final InitConfigAction initConfigAction;

    @Inject
    public ArgParse4JArgParser(Package pkg, PingAction pingAction, InitConfigAction initConfigAction) {
        this.pkg = pkg;
        this.pingAction = pingAction;
        this.initConfigAction = initConfigAction;
    }

    @Override
    public ArgumentParser create() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Sentry");
        parser.version("${prog}: " + pkg.getImplementationVersion());
        parser.addArgument("-v", "--version").action(Arguments.version());
        parser.addArgument("-p", "--ping").action(pingAction);
        parser.addArgument("--init-config").action(initConfigAction).help(InitConfigAction.HELP);
        return parser;
    }
}
