package net.sghill.ci.sentry.cli;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;

public class ArgParse4JArgParser implements ArgParser<ArgumentParser> {
    @Override
    public ArgumentParser create() {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Sentry");
        Package pkg = getClass().getPackage();
        parser.version("${prog}: " + pkg.getImplementationVersion());
        parser.addArgument("-v", "--version").action(Arguments.version());
        return parser;
    }
}
