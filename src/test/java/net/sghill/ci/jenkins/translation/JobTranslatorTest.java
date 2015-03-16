package net.sghill.ci.jenkins.translation;

import com.google.common.collect.Lists;
import net.sghill.ci.jenkins.api.JenkinsBuild;
import net.sghill.ci.jenkins.api.JenkinsBuildResult;
import net.sghill.ci.jenkins.api.JenkinsJob;
import net.sghill.ci.sentry.audit.Auditor;
import net.sghill.ci.sentry.audit.Clock;
import net.sghill.ci.sentry.domain.Build;
import net.sghill.ci.sentry.domain.BuildResult;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobTranslatorTest {
    public static final DateTime JUNE_20_2010 = new DateTime(2010, 6, 20, 0, 59, DateTimeZone.UTC);
    @Mock
    private Clock clock;
    @Mock
    private Auditor auditor;
    @Mock
    private Map<String, Long> mostRecentBuild;
    @Mock
    private Logger logger;

    private JobTranslator translator;

    @Before
    public void setUp() {
        initMocks(this);
        given(clock.now()).willReturn(JUNE_20_2010);
        given(mostRecentBuild.get(anyString())).willReturn(0L);
        translator = new JobTranslator(auditor, mostRecentBuild, logger);
    }

    @Test
    public void shouldTranslateJobToBuilds() {
        // Given
        JenkinsBuild passed = new JenkinsBuild(false, 10000L, 2L, JenkinsBuildResult.SUCCESS, 999999999L);
        JenkinsBuild failed = new JenkinsBuild(false, 100L, 3L, JenkinsBuildResult.FAILURE, 77777777L);
        JenkinsJob job = new JenkinsJob("some-jenkins-job", Lists.newArrayList(passed, failed));

        // When
        Set<Build> builds = translator.translate(job);

        // Then
        verify(auditor, times(2)).stamp();
        assertThat(builds).containsOnly(
                new Build(
                        "some-jenkins-job:2",
                        "some-jenkins-job",
                        2L,
                        10000L,
                        new DateTime(999999999L, DateTimeZone.UTC),
                        BuildResult.PASSED,
                        1,
                        null), // AuditInfo not part of equals
                new Build(
                        "some-jenkins-job:3",
                        "some-jenkins-job",
                        3L,
                        100L,
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

    @Test
    public void shouldNotTranslateExistingJobs() {
        // Given
        given(mostRecentBuild.get("some-jenkins-job")).willReturn(6L);
        JenkinsJob job = new JenkinsJob("some-jenkins-job", Lists.newArrayList(new JenkinsBuild(true, null, 5L, null, null)));

        // When
        Set<Build> builds = translator.translate(job);

        // Then
        assertThat(builds).isEmpty();
    }
}
