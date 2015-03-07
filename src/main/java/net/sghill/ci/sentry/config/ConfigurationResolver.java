package net.sghill.ci.sentry.config;

import java.net.URL;

public interface ConfigurationResolver {
    String name();
    boolean canResolve();
    URL resolve();
}
