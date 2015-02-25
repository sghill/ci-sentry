package net.sghill.ci.sentry.identity;

public interface IdentityProvider<T> {
    T newId();
}
