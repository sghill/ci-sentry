package net.sghill.ci.sentry.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaPropertySystemConfiguration implements SystemConfiguration {
    @Override
    public Path getUserHome() {
        return Paths.get(System.getProperty("user.home"));
    }
}
