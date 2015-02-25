package net.sghill.ci.jenkins.api;

import net.sghill.ci.sentry.domain.BuildResult;

public enum JenkinsBuildResult {
    SUCCESS(BuildResult.PASSED), FAILURE(BuildResult.FAILED);

    private final BuildResult result;

    private JenkinsBuildResult(BuildResult result) {
        this.result = result;
    }

    public BuildResult translate() {
        return result;
    }
}
