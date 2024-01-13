package com.odeyalo.sonata.cello.spring.configuration.security.customizer;

import com.odeyalo.sonata.cello.spring.configuration.security.SecurityConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Interface is used to customize the {@link ServerHttpSecurity} used by Cello.
 * Will be invoked by {@link SecurityConfiguration} before creating the {@link SecurityWebFilterChain}
 */
public interface CelloOauth2SecurityCustomizer extends Customizer<ServerHttpSecurity> {
}
