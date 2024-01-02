package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.spring.configuration.security.configurer.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;

@Configuration
public class DefaultSecurityConfigurersConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = ServerHttpSecurity.CsrfSpec.class, parameterizedContainer = Customizer.class)
    public Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecCustomizer() {
        return new DisabledCsrfConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean(value = ServerHttpSecurity.FormLoginSpec.class, parameterizedContainer = Customizer.class)
    public Customizer<ServerHttpSecurity.FormLoginSpec> formLoginSpecCustomizer() {
        return new DisabledFormLoginConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean(value = ServerHttpSecurity.CorsSpec.class, parameterizedContainer = Customizer.class)
    public Customizer<ServerHttpSecurity.CorsSpec> corsSpecCustomizer() {
        return new DisabledCorsConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean(value = ServerHttpSecurity.ExceptionHandlingSpec.class, parameterizedContainer = Customizer.class)
    public Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingSpecCustomizer(ServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {
        return new CelloExceptionHandlingConfigurer(serverAuthenticationEntryPoint);
    }

    @Bean
    @ConditionalOnMissingBean(value = ServerHttpSecurity.AuthorizeExchangeSpec.class, parameterizedContainer = Customizer.class)
    public Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecCustomizer() {
        return new CelloAuthorizeExchangeConfigurer();
    }

}
