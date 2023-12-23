package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

/**
 * Represent any possible oauth2 successful authorization response that should be returned to the client
 */
public interface Oauth2AuthorizationResponse<T extends Oauth2AuthorizationRequest> {
    /**
     * All responses must have associated request.
     * Made it with generic to reduce casting complexity and make code more readable
     * @return - associated request with this respose
     */
    @NotNull
    T getAssociatedRequest();

}