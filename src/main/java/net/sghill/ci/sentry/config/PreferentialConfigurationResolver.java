package net.sghill.ci.sentry.config;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.URL;

public class PreferentialConfigurationResolver implements ConfigurationResolver {
    public static final String CANNOT_RESOLVE_MESSAGE = "No configuration found at {}";
    public static final String RESOLVED_MESSAGE = "Successfully resolved configuration from {}";
    private final Iterable<ConfigurationResolver> resolvers;
    private final Logger logger;

    @Inject
    public PreferentialConfigurationResolver(Iterable<ConfigurationResolver> resolvers, Logger logger) {
        this.resolvers = resolvers;
        this.logger = logger;
    }

    @Override
    public URL resolve() {
        for (ConfigurationResolver resolver : resolvers) {
            if(resolver.canResolve()) {
                logger.info(RESOLVED_MESSAGE, resolver.name());
                return resolver.resolve();
            }
            logger.info(CANNOT_RESOLVE_MESSAGE, resolver.name());
        }
        throw new IllegalArgumentException("No resolvers loaded that are available to resolve");
    }

    @Override
    public String name() {
        return "Preferential Configuration Selector";
    }

    @Override
    public boolean canResolve() {
        return true;
    }
}
