package net.sghill.ci.sentry.cli;

import lombok.RequiredArgsConstructor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;

@RequiredArgsConstructor
public class ArgParse4JArgParser implements ArgParser<ArgumentParser> {
    private final Package pkg;

    @Override
    public ArgumentParser create() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Sentry");
        parser.version("${prog}: " + pkg.getImplementationVersion());
        parser.addArgument("-v", "--version").action(Arguments.version());
        return parser;
    }
}
