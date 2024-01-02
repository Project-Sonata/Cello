package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.spring.configuration.security.configurer.DisabledCorsConfigurer;
import com.odeyalo.sonata.cello.spring.configuration.security.configurer.DisabledCsrfConfigurer;
import com.odeyalo.sonata.cello.spring.configuration.security.configurer.DisabledFormLoginConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

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

}
