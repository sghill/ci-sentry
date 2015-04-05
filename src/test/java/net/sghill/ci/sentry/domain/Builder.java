package net.sghill.ci.sentry.domain;

import com.google.common.io.BaseEncoding;
import org.joda.time.DateTime;

import java.security.SecureRandom;

public abstract class Builder<T> {
    public static final BaseEncoding ENCODING = BaseEncoding.base32Hex().omitPadding();
    private final SecureRandom random = new SecureRandom();

    public abstract T build();

    public String randomString() {
        byte[] b = new byte[16];
        random.nextBytes(b);
        return ENCODING.encode(b);
    }

    public Integer randomInt() {
        return random.nextInt();
    }

    public Long randomLong() {
        return random.nextLong();
    }

    public DateTime randomDateTime() {
        return new DateTime(random.nextLong());
    }
}
