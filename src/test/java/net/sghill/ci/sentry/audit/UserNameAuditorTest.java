package net.sghill.ci.sentry.audit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserNameAuditorTest {
    @Mock
    private Clock clock;
    @Mock
    private SystemProvider systemProvider;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldUseCurrentDateTime() {
        // Given
        UserNameAuditor auditor = new UserNameAuditor(clock, systemProvider);

        // When
        auditor.stamp();

        // Then
        verify(clock).now();
        verify(systemProvider).userName();
    }
}
