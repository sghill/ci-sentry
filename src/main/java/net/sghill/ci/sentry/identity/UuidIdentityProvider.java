package net.sghill.ci.sentry.identity;

import java.util.UUID;

public class UuidIdentityProvider implements IdentityProvider<UUID> {
    @Override
    public UUID newId() {
        return UUID.randomUUID();
    }
}
