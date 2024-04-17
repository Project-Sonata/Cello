package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Strategy to save a {@link Oauth2AuthenticationMetadata} that shared between oauth2 requests.
 */
public interface Oauth2AuthenticationMetadataRepository {
    /**
     * Saves a {@link Oauth2AuthenticationMetadata} in repository, override existing one if already exist
     *
     * @param id       - id to associate a metadata with, in most cases it is a oauth2 state
     * @param metadata - metadata to save
     * @return - {@link Mono} {@link Void} on success, {@link Mono} with error if failed
     */
    @NotNull
    Mono<Void> save(@NotNull String id, @NotNull Oauth2AuthenticationMetadata metadata);

    /**
     * Searches for a {@link Oauth2AuthenticationMetadata} in repository by provided ID
     *
     * @param id - id that associated with {@link Oauth2AuthenticationMetadata}
     * @return - {@link  Mono} with {@link  Oauth2AuthenticationMetadata} if found, empty {@link Mono} otherwise
     */
    @NotNull
    Mono<Oauth2AuthenticationMetadata> findBy(@NotNull String id);

    /**
     * Removes associated {@link Oauth2AuthenticationMetadata} with a provided ID from a repository
     * @param id - ID associated with {@link Oauth2AuthenticationMetadata}
     * @return - {@link Mono} with deleted {@link Oauth2AuthenticationMetadata} or empty {@link Mono} if ID is not associated with {@link Oauth2AuthenticationMetadata}
     */
    @NotNull
    Mono<Oauth2AuthenticationMetadata> remove(@NotNull String id);


}
