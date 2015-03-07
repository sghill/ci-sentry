package net.sghill.ci.sentry.config;

import com.google.common.io.Resources;

import java.net.URL;

public class ClasspathDefaultConfigurationResolver implements ConfigurationResolver {
    @Override
    public String name() {
        return "Classpath";
    }

    @Override
    public boolean canResolve() {
        return true;
    }

    @Override
    public URL resolve() {
        return Resources.getResource("default.yml");
    }
}
