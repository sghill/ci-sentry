package net.sghill.ci.sentry.cli.actions.init;

public enum InitConfigResult {
    CREATED("Configuration successfully created at {}"),
    ALREADY_EXISTED("No change - file already existed at {}"),
    ERROR("There was a problem serializing the configuration to {}");
    private final String message;

    private InitConfigResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
