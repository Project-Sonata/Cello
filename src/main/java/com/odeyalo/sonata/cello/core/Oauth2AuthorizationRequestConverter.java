package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.exception.MalformedOauth2RequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link org.springframework.web.server.ServerWebExchange} to specific {@link Oauth2AuthorizationRequest}
 */
public interface Oauth2AuthorizationRequestConverter {

    /**
     * Convert the {@link ServerWebExchange} to specific {@link Oauth2AuthorizationRequest}, if not supported by implementation then empty {@link Mono}
     *
     * @param exchange - current exchange
     * @return - {@link Mono} with converted {@link Oauth2AuthorizationRequest}, empty {@link Mono} if not supported,
     * or {@link Mono} with error if request is malformed
     * @throws MalformedOauth2RequestException - if the request is malformed and cannot be constructed
     */
    @NotNull
    Mono<Oauth2AuthorizationRequest> convert(@NotNull ServerWebExchange exchange);

}
