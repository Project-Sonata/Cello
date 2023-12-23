package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Used to store any {@link AccessToken} that should be accessed later.
 */
public interface AccessTokenStore {
    /**
     * Save the given access token
     * @param accessToken - access token to save
     * @return - {@link Mono} with {@link Void} on success or {@link Mono} with error
     */
    @NotNull
    Mono<Void> saveToken(@NotNull AccessToken accessToken);

    /**
     * Looking for token in store by its value
     * @param tokenValue - token value to use for searching
     * @return - {@link Mono} with {@link AccessToken} if found or empty {@link Mono} if not found
     */
    @NotNull
    Mono<AccessToken> findTokenByTokenValue(@NotNull String tokenValue);

    /**
     * Indicates if token exists by this value or no
     * @param tokenValue - token value to check
     * @return - {@link Mono} with {@link Boolean#TRUE} if exist,
     * {@link Mono} with {@link Boolean#FALSE} otherwise
     */
    @NotNull
    Mono<Boolean> exists(@NotNull String tokenValue);
}
