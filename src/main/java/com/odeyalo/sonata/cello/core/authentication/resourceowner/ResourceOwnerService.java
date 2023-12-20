package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Load the resource owner by its username.
 * Used as analog for UserDetailsService in Spring Security but focuses only on resource owners.
 */
public interface ResourceOwnerService {

    /**
     * Load the resource owner by username.
     * @param username - username to use to load the user
     * @return - populated {@link Mono} with {@link ResourceOwner} on success,
     * empty {@link Mono} if resource owner with given username does not exist
     *
     * Does not throw any exception
     */
    @NotNull
    Mono<ResourceOwner> loadResourceOwnerByUsername(@NotNull String username);

}
