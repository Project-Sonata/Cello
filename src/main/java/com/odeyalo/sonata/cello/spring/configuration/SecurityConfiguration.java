package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.spring.auth.CelloOauth2AuthenticationConverter;
import com.odeyalo.sonata.cello.spring.auth.CelloOauth2AuthenticationManager;
import com.odeyalo.sonata.cello.web.AuthorizationRequestHandlerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;

@Configuration
public class SecurityConfiguration {
    private final AuthorizationRequestHandlerFilter authorizationRequestValidationFilter;

    public SecurityConfiguration(AuthorizationRequestHandlerFilter authorizationRequestHandlerFilter) {
        this.authorizationRequestValidationFilter = authorizationRequestHandlerFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         CelloOauth2AuthenticationManager manager,
                                                         ServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);

        authenticationWebFilter.setServerAuthenticationConverter(new CelloOauth2AuthenticationConverter());

        return httpSecurity
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterBefore(authorizationRequestValidationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint(
                        serverAuthenticationEntryPoint
                ))
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.pathMatchers("/login").permitAll()
                                .anyExchange().authenticated())
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint(ServerRequestCache cache) {
        RedirectServerAuthenticationEntryPoint entryPoint = new RedirectServerAuthenticationEntryPoint("/login");
        entryPoint.setRequestCache(cache);
        return entryPoint;
    }
}
