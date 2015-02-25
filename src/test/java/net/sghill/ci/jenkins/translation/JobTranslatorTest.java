package net.sghill.ci.jenkins.translation;

import com.google.common.collect.Lists;
import net.sghill.ci.jenkins.api.JenkinsBuild;
import net.sghill.ci.jenkins.api.JenkinsBuildResult;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.sentry.audit.Auditor;
import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.domain.BuildResult;
import net.sghill.ci.sentry.identity.IdentityProvider;
import net.sghill.ci.sentry.audit.Clock;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobTranslatorTest {
    public static final DateTime JUNE_20_2010 = new DateTime(2010, 6, 20, 0, 59, DateTimeZone.UTC);
    @Mock
    private Clock clock;
    @Mock
    private IdentityProvider<UUID> identityProvider;
    @Mock
    private Auditor auditor;

    private JobTranslator translator;

    @Before
    public void setUp() {
        initMocks(this);
        given(clock.now()).willReturn(JUNE_20_2010);
        translator = new JobTranslator(identityProvider, auditor);
    }

    @Test
    public void shouldTranslateJobToBuilds() {
        // Given
        UUID one = UUID.randomUUID();
        UUID two = UUID.randomUUID();
        given(identityProvider.newId()).willReturn(one, two);
        JenkinsBuild passed = new JenkinsBuild(false, 10000L, 2L, JenkinsBuildResult.SUCCESS, 999999999L);
        JenkinsBuild failed = new JenkinsBuild(false, 100L, 3L, JenkinsBuildResult.FAILURE, 77777777L);
        JenkinsJob job = new JenkinsJob("some-jenkins-job", Lists.newArrayList(passed, failed));

        // When
        Set<Build> builds = translator.translate(job);

        // Then
        verify(identityProvider, times(2)).newId();
        verify(auditor, times(2)).stamp();
        assertThat(builds).containsOnly(
                new Build(
                        null, // Id is not part of equals
                        "some-jenkins-job",
                        "2",
                        new Duration(10000L),
                        new DateTime(999999999L, DateTimeZone.UTC),
                        BuildResult.PASSED,
                        1,
                        null), // AuditInfo not part of equals
                new Build(
                        null,
                        "some-jenkins-job",
                        "3",
                        new Duration(100L),
                        new DateTime(77777777L, DateTimeZone.UTC),
                        BuildResult.FAILED,
                        1,
                        null
                ));
    }

    @Test
    public void shouldNotTranslateJobsInProgress() {
        // Given
        JenkinsJob job = new JenkinsJob("some-jenkins-job", Lists.newArrayList(new JenkinsBuild(true, null, null, null, null)));

        // When
        Set<Build> builds = translator.translate(job);

        // Then
        assertThat(builds).isEmpty();
    }
}
