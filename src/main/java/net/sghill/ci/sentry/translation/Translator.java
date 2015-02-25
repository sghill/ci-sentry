package net.sghill.ci.sentry.translation;

public interface Translator<S, T> {
    T translate(S s);
}
