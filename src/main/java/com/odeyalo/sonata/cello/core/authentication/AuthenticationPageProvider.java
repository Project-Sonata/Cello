package com.odeyalo.sonata.cello.core.authentication;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Return the {@link ServerHttpResponse} to return to the user where user can authenticate himself
 * Used as delegate for GET "/oauth2/login" endpoint
 */
public interface AuthenticationPageProvider {

    @NotNull
    Mono<ServerHttpResponse> getAuthenticationPage(@NotNull ServerWebExchange exchange);

}
