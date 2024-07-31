package com.odeyalo.sonata.cello.core.grant;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * A strategy to handle the grant type based on {@link AccessTokenRequest}
 */
public interface GrantHandler {
    /**
     * Check if the given request is supported by this handler
     * {@link #handle(AccessTokenRequest)} only if this method returned true
     * @param accessTokenRequest - request to check
     * @return {@link Mono} with {@code true} if this handler supports,
     * with {@code false} otherwise
     */
    @NotNull
    Mono<Boolean> supports(@NotNull AccessTokenRequest accessTokenRequest);

    /**
     * Handle the given request and return the {@link AccessTokenResponse}
     * @param accessTokenRequest - request to handle
     * @return {@link Mono} with {@link AccessTokenResponse} on success,
     * {@link Mono#error(Throwable)} with error on any exception
     */
    @NotNull
    Mono<AccessTokenResponse> handle(@NotNull AccessTokenRequest accessTokenRequest);

}
