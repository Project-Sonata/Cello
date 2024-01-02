package com.odeyalo.sonata.cello.spring.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;

/**
 * Support configuration to create bean that can be created in {@link com.odeyalo.sonata.cello.spring.configuration.SecurityConfiguration} because they are in creation
 */
@Configuration
public class SecurityConfigurationSupport {

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint(ServerRequestCache cache) {
        RedirectServerAuthenticationEntryPoint entryPoint = new RedirectServerAuthenticationEntryPoint("/login");
        entryPoint.setRequestCache(cache);
        return entryPoint;
    }
}
