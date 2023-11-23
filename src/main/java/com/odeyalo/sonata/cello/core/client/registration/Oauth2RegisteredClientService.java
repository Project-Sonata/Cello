package com.odeyalo.sonata.cello.core.client.registration;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Base interface to load the registered Oauth2 instances in Cello
 */
public interface Oauth2RegisteredClientService {
    /**
     * Load the Oauth2RegisteredClient associated with the given client id.
     * @param clientId - client id associated with Oauth2RegisteredClient
     * @return - found Oauth2RegisteredClient or empty Mono if nothing is found.
     * Never throw any exception.
     */
    @NotNull
    Mono<Oauth2RegisteredClient> findByClientId(@NotNull String clientId);

}
