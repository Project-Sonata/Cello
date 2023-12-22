package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link Oauth2AuthorizationResponse} of specific type to the {@link org.springframework.web.server.ServerWebExchange}
 */
public interface Oauth2AuthorizationResponseConverter {

    Mono<ServerHttpResponse> convert(@NotNull Oauth2AuthorizationExchange exchange,
                                     @NotNull ServerWebExchange currentExchange);

}
