package com.odeyalo.sonata.cello.spring.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Support configuration to create bean that can be created in {@link SecurityConfiguration} because they are in creation
 */
@Configuration
public class SecurityConfigurationSupport {

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {
        return new Oauth2FlowIdAwareRedirectServerAuthenticationEntryPoint();
    }

    /**
     * Redirect the resource owner to /login endpoint with 'flow_id' parameter
     */
    private static class Oauth2FlowIdAwareRedirectServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
        private final ServerRedirectStrategy serverRedirectStrategy = new DefaultServerRedirectStrategy();

        @Override
        public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
            String flowId = (String) exchange.getAttributes().get("flow_id");
            if ( flowId == null ) {
                return Mono.error(new IllegalStateException("Flow id is required by standard!"));
            }
            return serverRedirectStrategy.sendRedirect(exchange, URI.create("/oauth2/login?flow_id=" + flowId));
        }
    }
}
