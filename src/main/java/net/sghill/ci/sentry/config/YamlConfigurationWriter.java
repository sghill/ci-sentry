package net.sghill.ci.sentry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sghill.ci.sentry.FileSystem;
import net.sghill.ci.sentry.cli.actions.init.InitConfigResult;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Path;

public class YamlConfigurationWriter implements ConfigurationWriter {
    private final FileSystem fileSystem;
    private final ObjectMapper objectMapper;
    private final SentryConfiguration configuration;

    @Inject
    public YamlConfigurationWriter(FileSystem fileSystem, @Named("YAML") ObjectMapper objectMapper, SentryConfiguration configuration) {
        this.fileSystem = fileSystem;
        this.objectMapper = objectMapper;
        this.configuration = configuration;
    }

    @Override
    public InitConfigResult writeDefaultConfigurationTo(Path path) {
        if (fileSystem.doesNotExist(path)) {
            fileSystem.createFile(path);
            try {
                objectMapper.writeValue(path.toFile(), configuration);
                return InitConfigResult.CREATED;
            } catch (IOException e) {
                return InitConfigResult.ERROR;
            }
        }
        return InitConfigResult.ALREADY_EXISTED;
    }
}
