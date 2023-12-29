package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Strategy used to handle a failed resource owner authentication attempt.
 */
public interface ResourceOwnerAuthenticationFailureHandler {

    /**
     * Called when an authentication attempt fails.
     * @param exchange - current http exchange
     * @param authenticationException - occurred {@link ResourceOwnerAuthenticationException}
     * @return - {@link Mono} with {@link Void}
     */
    @NotNull
    Mono<Void> onAuthenticationFailure(@NotNull ServerWebExchange exchange,
                                       @NotNull ResourceOwnerAuthenticationException authenticationException);
}
