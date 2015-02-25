package net.sghill.ci.sentry.audit;

import org.joda.time.DateTime;

public interface Clock {
    DateTime now();
}
