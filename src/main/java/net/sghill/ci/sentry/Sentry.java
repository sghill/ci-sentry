package net.sghill.ci.sentry;

import dagger.ObjectGraph;
import net.sghill.ci.sentry.cli.ArgParse4JArgParser;
import net.sghill.ci.sentry.cli.Command;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.Map;

public class Sentry {
    @Inject
    ArgParse4JArgParser parser;
    @Inject
    Map<Command, Runnable> actionFactory;

    public static void main(String[] args) throws Exception {
        new Sentry().run(args);
    }

    public void run(String[] args) throws URISyntaxException, ArgumentParserException {
        ObjectGraph.create(new SentryModule()).inject(this);
        Namespace namespace = parser.create().parseArgsOrFail(args);
        Command c = namespace.get(ArgParse4JArgParser.CMD);
        actionFactory.get(c).run();
    }
}
