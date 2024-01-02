package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.spring.configuration.security.configurer.DisabledCsrfConfigurer;
import com.odeyalo.sonata.cello.spring.configuration.security.configurer.DisabledFormLoginConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@Configuration
public class DefaultConfigurersConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecCustomizer() {
        return new DisabledCsrfConfigurer();
    }

    @Bean
    @ConditionalOnMissingBean
    public Customizer<ServerHttpSecurity.FormLoginSpec> formLoginSpecCustomizer() {
        return new DisabledFormLoginConfigurer();
    }

}
