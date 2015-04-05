package net.sghill.ci.sentry.cli;

import net.sghill.ci.sentry.cli.actions.init.InitAction;
import net.sghill.ci.sentry.cli.actions.ping.PingAction;
import net.sghill.ci.sentry.cli.actions.record.RecordBuildsAction;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.Subparsers;

import javax.inject.Inject;

public class ArgParse4JArgParser implements ArgParser<ArgumentParser> {
    public static final String CMD = "cmd";
    private final Package pkg;

    @Inject
    public ArgParse4JArgParser(Package pkg) {
        this.pkg = pkg;
    }

    @Override
    public ArgumentParser create() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Sentry");
        parser.version("${prog}: " + pkg.getImplementationVersion());
        parser.addArgument("-v", "--version")
                .action(Arguments.version())
                .help("show application version and exit");
        Subparsers subparsers = parser.addSubparsers().title("subcommands").metavar("COMMAND");
        subparsers.addParser("ping")
                .setDefault(CMD, Command.PING)
                .help(PingAction.HELP);
        subparsers.addParser("init")
                .setDefault(CMD, Command.INIT)
                .help(InitAction.HELP);
        subparsers.addParser("record")
                .setDefault(CMD, Command.RECORD)
                .help(RecordBuildsAction.HELP);
        subparsers.addParser("report")
                .setDefault(CMD, Command.REPORT);
        return parser;
    }
}
