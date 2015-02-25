package net.sghill.ci.sentry.audit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class UtcClock implements Clock {
    @Override
    public DateTime now() {
        return DateTime.now(DateTimeZone.UTC);
    }
}
