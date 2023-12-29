package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Strategy to handle success resource owner authentication
 */
public interface ResourceOwnerAuthenticationSuccessHandler {

    /**
     * Called when a resource owner has been successfully authenticated
     * @param exchange - current http exchange
     * @param authentication - authentication representing the resource owner
     * @return - {@link Mono} with {@link Void}
     */
    @NotNull
    Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange exchange,
                                       @NotNull AuthenticatedResourceOwnerAuthentication authentication);

}
