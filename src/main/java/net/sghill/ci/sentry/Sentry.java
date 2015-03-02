package net.sghill.ci.sentry;

import com.google.common.io.Resources;
import dagger.ObjectGraph;
import net.sghill.ci.sentry.cli.ArgParser;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.inject.Inject;
import java.net.URISyntaxException;

public class Sentry {
    @Inject
    ArgParser<ArgumentParser> parser;

    public static void main(String[] args) throws Exception {
        new Sentry().run(args);
    }

    public void run(String[] args) throws URISyntaxException, ArgumentParserException {
        ObjectGraph.create(new SentryModule(Resources.getResource("default.yml"))).inject(this);
        Namespace namespace = parser.create().parseArgs(args);
    }
}
