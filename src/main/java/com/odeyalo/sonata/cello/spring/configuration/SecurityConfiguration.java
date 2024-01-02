package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.web.AuthenticationLoaderFilter;
import com.odeyalo.sonata.cello.web.AuthorizationRequestHandlerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
public class SecurityConfiguration {
    private final AuthorizationRequestHandlerFilter authorizationRequestValidationFilter;

    @Autowired
    Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.FormLoginSpec> formLoginSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.CorsSpec> corsSpecCustomizer;
    @Autowired
    Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingSpecCustomizer;

    public SecurityConfiguration(AuthorizationRequestHandlerFilter authorizationRequestHandlerFilter) {
        this.authorizationRequestValidationFilter = authorizationRequestHandlerFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         AuthenticationLoaderFilter authenticationLoaderFilter,
                                                         ServerSecurityContextRepository securityContextRepository) {

        return httpSecurity
                .formLogin(formLoginSpecCustomizer)
                .csrf(csrfSpecCustomizer)
                .cors(corsSpecCustomizer)
                .addFilterBefore(authorizationRequestValidationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(authenticationLoaderFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpecCustomizer)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.pathMatchers("/login").permitAll()
                                .anyExchange().authenticated())
                .build();
    }
}
