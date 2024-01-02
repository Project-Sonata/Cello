package com.odeyalo.sonata.cello.spring.configuration.security.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * Disable the {@link org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec}
 */
public class DisabledCsrfConfigurer implements Customizer<ServerHttpSecurity.CsrfSpec> {

    @Override
    public void customize(ServerHttpSecurity.CsrfSpec csrfSpec) {
        csrfSpec.disable();
    }
}
