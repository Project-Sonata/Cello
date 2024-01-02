package com.odeyalo.sonata.cello.spring.configuration.security.customizer;

import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.List;

/**
 * Invoke list of {@link CelloOauth2SecurityCustomizer}s.
 */
public class CompositeCelloOauth2SecurityCustomizer implements CelloOauth2SecurityCustomizer {
    private final List<CelloOauth2SecurityCustomizer> delegates;

    public CompositeCelloOauth2SecurityCustomizer(List<CelloOauth2SecurityCustomizer> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void customize(ServerHttpSecurity security) {
        delegates.forEach(customizer -> customizer.customize(security));
    }
}
