package net.sghill.ci.sentry;

import com.google.common.io.Resources;
import net.sghill.ci.jenkins.api.JenkinsBuild;
import dagger.ObjectGraph;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Sentry {
    @Inject
    JenkinsService jenkins;

    public static void main(String[] args) throws Exception {
        new Sentry().run(args);
    }

    public void run(String[] args) throws URISyntaxException {
        Path configuration = Paths.get(Resources.getResource("default.yml").toURI());
        ObjectGraph.create(new SentryModule(configuration.toFile())).inject(this);
        for (JenkinsBuild build : jenkins.fetchJobByName("long-job").getBuilds()) {
            System.out.println(build);
        }

    }
}
