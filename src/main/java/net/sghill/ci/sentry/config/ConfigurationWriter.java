package net.sghill.ci.sentry.config;

import net.sghill.ci.sentry.cli.actions.init.InitConfigResult;

import java.nio.file.Path;

public interface ConfigurationWriter {
    InitConfigResult writeDefaultConfigurationTo(Path path);
}
