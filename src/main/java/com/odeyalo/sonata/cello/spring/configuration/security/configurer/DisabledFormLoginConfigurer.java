package com.odeyalo.sonata.cello.spring.configuration.security.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

/**
 * Disable {@link org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec}
 */
public class DisabledFormLoginConfigurer implements Customizer<ServerHttpSecurity.FormLoginSpec> {

    @Override
    public void customize(ServerHttpSecurity.FormLoginSpec formLoginSpec) {
        formLoginSpec.disable();
    }
}
