package net.sghill.ci.sentry.audit;

public class JvmSystemProvider implements SystemProvider {
    @Override
    public String userName() {
        return System.getProperty("user.name");
    }
}
