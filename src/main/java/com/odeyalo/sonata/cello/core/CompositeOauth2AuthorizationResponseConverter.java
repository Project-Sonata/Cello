package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Invoke list of {@link Oauth2AuthorizationResponseConverter} and return first non-empty result.
 */
@Component
@Primary
public class CompositeOauth2AuthorizationResponseConverter implements Oauth2AuthorizationResponseConverter {
    private final List<Oauth2AuthorizationResponseConverter> converters;

    @Autowired
    public CompositeOauth2AuthorizationResponseConverter(List<Oauth2AuthorizationResponseConverter> converters) {
        this.converters = converters;
    }

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull final Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> response,
                                            @NotNull final ServerWebExchange currentExchange) {

        return Flux.fromIterable(converters)
                .flatMap(converter -> converter.convert(response, currentExchange))
                .next();
    }
}
