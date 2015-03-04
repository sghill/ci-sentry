package net.sghill.ci.sentry;

import java.nio.file.Path;

public interface FileSystem {
    Path createFile(Path path);
    boolean doesNotExist(Path path);
}
