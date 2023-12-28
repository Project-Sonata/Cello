package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationRequestValidationFilter implements WebFilter {
    private final Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter;
    private final Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator;

    public AuthorizationRequestValidationFilter(Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter, Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator) {
        this.oauth2AuthorizationRequestConverter = oauth2AuthorizationRequestConverter;
        this.oauth2AuthorizationRequestValidator = oauth2AuthorizationRequestValidator;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull ServerWebExchange exchange,
                             @NotNull WebFilterChain chain) {
        return oauth2AuthorizationRequestConverter.convert(exchange)
                .flatMap(oauth2AuthorizationRequestValidator::validate)
                .then(chain.filter(exchange));
    }
}
