package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Authenticate the resource owner with the given {@link ServerWebExchange}
 * return the {@link ServerHttpResponse} that will be returned to the user.
 *
 * Used as delegate in '/oauth2/login/ endpoint
 */
public interface ResourceOwnerAuthenticator {
    /**
     * Tries to authenticate resource owner using the given {@link ServerWebExchange}
     * @param webExchange - current exchange
     * @return - mono with {@link ServerHttpResponse}
     */
    Mono<ServerHttpResponse> authenticate(ServerWebExchange webExchange);
}