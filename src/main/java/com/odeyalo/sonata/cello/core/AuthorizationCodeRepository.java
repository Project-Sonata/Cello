package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Repository to save the generated authorization codes
 */
public interface AuthorizationCodeRepository {
    /**
     * Save the given generated authorization code
     * @param authorizationCode - code to save
     * @return {@link Mono} with {@link Void} on success,
     * {@link Mono#error(Throwable)} on error
     */
    @NotNull
    Mono<Void> save(@NotNull GeneratedAuthorizationCode authorizationCode);

    /**
     * Try to find an authorization code by value
     * @param authorizationCodeValue - unique authorization code identifier
     * @return - a {@link Mono} with found {@link GeneratedAuthorizationCode},
     * or {@link Mono#empty()} if code associated with this value does not exist
     */
    @NotNull
    Mono<GeneratedAuthorizationCode> findByCodeValue(@NotNull String authorizationCodeValue);

    /**
     * Delete the authorization code by its value, do nothing if code associated with this value does not exist
     * @param authorizationCodeValue - code associated with this value to delete
     * @return {@link Mono) with {@link Void} in any case
     */
    @NotNull
    Mono<Void> deleteByCodeValue(@NotNull String authorizationCodeValue);
}