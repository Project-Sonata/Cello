package com.odeyalo.sonata.cello.spring.configuration.security.configurer;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

public class CelloAuthorizeExchangeConfigurer implements Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> {

    @Override
    public void customize(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        authorizeExchangeSpec
                .pathMatchers("/oauth2/login").permitAll()
                .anyExchange().authenticated();
    }
}
