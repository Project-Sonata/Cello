package com.odeyalo.sonata.cello.spring.configuration.security.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

public class DisabledCorsConfigurer implements Customizer<ServerHttpSecurity.CorsSpec> {

    @Override
    public void customize(ServerHttpSecurity.CorsSpec corsSpec) {
        corsSpec.disable();
    }
}
