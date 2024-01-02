package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.spring.configuration.security.customizer.CelloOauth2SecurityCustomizer;
import com.odeyalo.sonata.cello.web.AuthenticationLoaderFilter;
import com.odeyalo.sonata.cello.web.AuthorizationRequestHandlerFilter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfiguration {
    @Autowired
    Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.FormLoginSpec> formLoginSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.CorsSpec> corsSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecConfigurer;
    @Autowired
    CelloOauth2SecurityCustomizer oauth2SecurityCustomizer;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         AuthorizationRequestHandlerFilter authorizationRequestHandlerFilter,
                                                         AuthenticationLoaderFilter authenticationLoaderFilter,
                                                         ServerSecurityContextRepository securityContextRepository) {

        ServerHttpSecurity serverHttpSecurity = httpSecurity
                .formLogin(formLoginSpecConfigurer)
                .csrf(csrfSpecConfigurer)
                .cors(corsSpecConfigurer)
                .addFilterBefore(authorizationRequestHandlerFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(authenticationLoaderFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpecConfigurer)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(authorizeExchangeSpecConfigurer);

        oauth2SecurityCustomizer.customize(serverHttpSecurity);

        return serverHttpSecurity.build();

    }
}
