package net.sghill.ci.sentry.cli;

import lombok.RequiredArgsConstructor;
import net.sghill.ci.sentry.cli.actions.ping.PingAction;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;

@RequiredArgsConstructor
public class ArgParse4JArgParser implements ArgParser<ArgumentParser> {
    private final Package pkg;
    private final PingAction pingAction;

    @Override
    public ArgumentParser create() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Sentry");
        parser.version("${prog}: " + pkg.getImplementationVersion());
        parser.addArgument("-v", "--version").action(Arguments.version());
        parser.addArgument("-p", "--ping").action(pingAction);
        return parser;
    }
}
