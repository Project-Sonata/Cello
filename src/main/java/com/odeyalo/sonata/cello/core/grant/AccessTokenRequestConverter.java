package com.odeyalo.sonata.cello.core.grant;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Strategy to convert the given {@link ServerWebExchange} to specific {@link AccessTokenRequest}
 */
public interface AccessTokenRequestConverter {
    /**
     * Convert the given {@link ServerWebExchange} to {@link AccessTokenRequest}
     * @param httpExchange - current http exchange
     * @return - a {@link Mono} emitting a {@link AccessTokenRequest},
     * {@link Mono#empty()} if the given converter does not support this type of grant,
     * {@link Mono#error(Throwable)} if request contains invalid payload(invalid parameters, missing parameters, etc.)
     * @throws com.odeyalo.sonata.cello.exception.MalformedAccessTokenRequestException - on invalid request
     */
    @NotNull
    Mono<AccessTokenRequest> convert(@NotNull ServerWebExchange httpExchange);

}
