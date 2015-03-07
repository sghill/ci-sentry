package net.sghill.ci.sentry.config;

import net.sghill.ci.sentry.FileSystem;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserHomeConfigurationResolver implements ConfigurationResolver {
    private static final Path FILENAME = Paths.get(".sentry.yml");
    private final SystemConfiguration systemConfiguration;
    private final FileSystem fileSystem;

    @Inject
    public UserHomeConfigurationResolver(SystemConfiguration systemConfiguration, FileSystem fileSystem) {
        this.systemConfiguration = systemConfiguration;
        this.fileSystem = fileSystem;
    }

    @Override
    public String name() {
        return getKnownLocation().toString();
    }

    @Override
    public boolean canResolve() {
        return !fileSystem.doesNotExist(getKnownLocation());
    }

    @Override
    public URL resolve() {
        try {
            return getKnownLocation().toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getKnownLocation() {
        return systemConfiguration.getUserHome().resolve(FILENAME);
    }
}
