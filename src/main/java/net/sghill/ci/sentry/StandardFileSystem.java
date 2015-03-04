package net.sghill.ci.sentry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StandardFileSystem implements FileSystem {
    @Override
    public Path createFile(Path path) {
        try {
            return Files.createFile(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean doesNotExist(Path path) {
        return !Files.exists(path);
    }
}
