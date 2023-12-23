package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link Oauth2AuthorizationResponse} of specific type to the {@link org.springframework.web.server.ServerWebExchange}
 */
public interface Oauth2AuthorizationResponseConverter {
    /**
     * Convert the given {@link Oauth2AuthorizationResponse} to the {@link ServerHttpResponse}
     * @param response - response that need to be converted
     * @param currentExchange - current http exchange
     * @return - {@link Mono} populated with {@link ServerHttpResponse} that should be returned to the end-user
     */

    @NotNull
    Mono<ServerHttpResponse> convert(@NotNull Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> response,
                                     @NotNull ServerWebExchange currentExchange);

}
