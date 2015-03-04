package net.sghill.ci.sentry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sghill.ci.sentry.FileSystem;
import net.sghill.ci.sentry.cli.actions.init.InitConfigResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class YamlConfigurationWriterTest {
    @Mock
    private FileSystem fileSystem;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private SentryConfiguration configuration;
    private YamlConfigurationWriter writer;

    @Before
    public void setUp() {
        initMocks(this);
        writer = new YamlConfigurationWriter(fileSystem, objectMapper, configuration);
    }

    @Test
    public void shouldNotCreateFileIfExists() throws IOException {
        // Given
        Path path = Paths.get("/some/random/path");
        given(fileSystem.doesNotExist(path)).willReturn(false);

        // When
        InitConfigResult result = writer.writeDefaultConfigurationTo(path);

        // Then
        verify(fileSystem, never()).createFile(path);
        verify(objectMapper, never()).writeValue(path.toFile(), configuration);
        assertThat(result).isEqualTo(InitConfigResult.ALREADY_EXISTED);
    }

    @Test
    public void shouldSerializeFileIfNotExists() throws IOException {
        // Given
        Path path = Paths.get("/some/random/path");
        given(fileSystem.doesNotExist(path)).willReturn(true);

        // When
        InitConfigResult result = writer.writeDefaultConfigurationTo(path);

        // Then
        verify(fileSystem).createFile(path);
        assertThat(result).isEqualTo(InitConfigResult.CREATED);
    }

    @Test
    public void shouldReturnErrorIfSerializationFails() throws IOException {
        // Given
        Path path = Paths.get("/some/random/path");
        given(fileSystem.doesNotExist(path)).willReturn(true);
        doThrow(IOException.class).when(objectMapper).writeValue(any(File.class), any(SentryConfiguration.class));

        // When
        InitConfigResult result = writer.writeDefaultConfigurationTo(path);

        // Then
        assertThat(result).isEqualTo(InitConfigResult.ERROR);
    }
}
