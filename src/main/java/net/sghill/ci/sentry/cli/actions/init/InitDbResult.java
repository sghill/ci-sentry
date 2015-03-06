package net.sghill.ci.sentry.cli.actions.init;

public enum InitDbResult {
    CREATED("Database created at {}"),
    ALREADY_EXISTED("No change - database already existed at {}"),
    ERROR("There was a problem creating the database at {}");

    private final String messageTemplate;

    private InitDbResult(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }
}
