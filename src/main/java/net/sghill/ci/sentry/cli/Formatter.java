package net.sghill.ci.sentry.cli;

public interface Formatter<T> {
    String format(Iterable<T> args);
}
