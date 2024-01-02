package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.spring.configuration.security.customizer.CelloOauth2SecurityCustomizer;
import com.odeyalo.sonata.cello.spring.configuration.security.customizer.CompositeCelloOauth2SecurityCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class DefaultSecurityCustomizersConfiguration {

    @Bean
    @Primary
    public CompositeCelloOauth2SecurityCustomizer compositeCelloOauth2SecurityCustomizer(List<CelloOauth2SecurityCustomizer> delegates) {
        return new CompositeCelloOauth2SecurityCustomizer(delegates);
    }
}
