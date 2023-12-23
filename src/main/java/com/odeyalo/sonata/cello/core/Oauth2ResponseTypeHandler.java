package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Handle specific Oauth2 response type.
 */
public interface Oauth2ResponseTypeHandler {

    @NotNull
    Mono<Boolean> supports(@NotNull Oauth2AuthorizationRequest authorizationRequest);

    /**
     * Invoked if and only if when {@link #supports(Oauth2AuthorizationRequest)} method returns true
     *
     * @param authorizationRequest - request to handle when permission has been granted by resource owner.
     *                             It is the same request as was provided in {@link #supports(Oauth2AuthorizationRequest)}
     * @param resourceOwner        - owner of this resource, in most cases, end-user
     * @return - {@link Mono} with wrapped {@link Oauth2AuthorizationResponse} if success,
     * {@link Mono} with error if any exception occurred
     */
    @NotNull
    Mono<Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest>> permissionGranted(@NotNull Oauth2AuthorizationRequest authorizationRequest,
                                                        @NotNull ResourceOwner resourceOwner);
}
