package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Invoke list of {@link Oauth2AuthorizationRequestConverter} and return first non-empty result.
 */
@Component
@Primary
public class CompositeOauth2AuthorizationRequestConverter implements Oauth2AuthorizationRequestConverter {
    private final List<Oauth2AuthorizationRequestConverter> converters;

    @Autowired
    public CompositeOauth2AuthorizationRequestConverter(List<Oauth2AuthorizationRequestConverter> converters) {
        this.converters = converters;
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationRequest> convert(@NotNull ServerWebExchange exchange) {
        return Flux.fromIterable(converters)
                .flatMap(converter -> converter.convert(exchange))
                .next();
    }
}
