package net.sghill.ci.sentry;

import com.google.common.collect.Lists;
import net.sghill.ci.sentry.cli.actions.init.InitDbResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class DatabaseServiceTest {
    public static final Response CREATED = new Response("http://some-url/", 201, "CREATED", Lists.<Header>newArrayList(), null);
    public static final Response CONFLICT = new Response("http://some-url/", 412, "CONFLICT", Lists.<Header>newArrayList(), null);
    public static final Response SERVER_ERROR = new Response("http://some-url/", 500, "INTERNAL SERVER ERROR", Lists.<Header>newArrayList(), null);
    @Mock
    private Database database;
    private DatabaseService service;

    @Before
    public void setUp() {
        initMocks(this);
        service = new DatabaseService(database);
    }

    @Test
    public void shouldMapSuccessResult() {
        // Given
        given(database.createDatabase()).willReturn(CREATED);

        // When
        InitDbResult result = service.ensureDatabaseExists();

        // Then
        assertThat(result).isEqualTo(InitDbResult.CREATED);
    }

    @Test
    public void shouldMapConflict() {
        // Given
        RetrofitError error = mock(RetrofitError.class);
        given(error.getResponse()).willReturn(CONFLICT);
        doThrow(error).when(database).createDatabase();

        // When
        InitDbResult result = service.ensureDatabaseExists();

        // Then
        assertThat(result).isEqualTo(InitDbResult.ALREADY_EXISTED);
    }

    @Test
    public void shouldMap500() {
        // Given
        RetrofitError error = mock(RetrofitError.class);
        given(error.getResponse()).willReturn(SERVER_ERROR);
        doThrow(error).when(database).createDatabase();

        // When
        InitDbResult result = service.ensureDatabaseExists();

        // Then
        assertThat(result).isEqualTo(InitDbResult.ERROR);
    }
}
