package net.sghill.ci.sentry.config;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PreferentialConfigurationResolverTest {
    @Mock
    private Logger logger;
    @Mock
    private ConfigurationResolver resolverOne;
    @Mock
    private ConfigurationResolver resolverTwo;
    @Mock
    private ConfigurationResolver resolverThree;
    private PreferentialConfigurationResolver resolver;

    @Before
    public void setUp() {
        initMocks(this);
        resolver = new PreferentialConfigurationResolver(Lists.newArrayList(resolverOne, resolverTwo, resolverThree), logger);
    }

    @Test
    public void shouldOnlyResolveFromResolversWhoGiveGreenLightInOrder() {
        // Given
        given(resolverOne.canResolve()).willReturn(false);
        given(resolverTwo.canResolve()).willReturn(true);

        // When
        resolver.resolve();

        // Then
        verify(resolverOne, never()).resolve();
        verify(resolverTwo).resolve();
        verify(resolverThree, never()).canResolve();
    }

    @Test
    public void shouldLogMessage() {
        // Given
        given(resolverOne.name()).willReturn("User Home Directory");
        given(resolverOne.canResolve()).willReturn(false);
        given(resolverTwo.name()).willReturn("Classpath");
        given(resolverTwo.canResolve()).willReturn(true);

        // When
        resolver.resolve();

        // Then
        verify(logger).info(PreferentialConfigurationResolver.CANNOT_RESOLVE_MESSAGE, resolverOne.name());
        verify(logger).info(PreferentialConfigurationResolver.RESOLVED_MESSAGE, resolverTwo.name());
    }
}
