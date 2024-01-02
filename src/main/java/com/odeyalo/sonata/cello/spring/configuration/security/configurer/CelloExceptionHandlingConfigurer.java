package com.odeyalo.sonata.cello.spring.configuration.security.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

public class CelloExceptionHandlingConfigurer implements Customizer<ServerHttpSecurity.ExceptionHandlingSpec> {
    private final ServerAuthenticationEntryPoint serverAuthenticationEntryPoint;

    public CelloExceptionHandlingConfigurer(ServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {
        this.serverAuthenticationEntryPoint = serverAuthenticationEntryPoint;
    }

    @Override
    public void customize(ServerHttpSecurity.ExceptionHandlingSpec exceptionHandlingSpec) {
        exceptionHandlingSpec.authenticationEntryPoint(serverAuthenticationEntryPoint);
    }
}
