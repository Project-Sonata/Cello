package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.ImplicitOauth2AuthorizationResponse.BEARER_TOKEN_TYPE;


/**
 * Handle only implicit response type as defined in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2">Implicit Grant type</a>
 */
@Component
public class ImplicitOauth2ResponseTypeHandler implements Oauth2ResponseTypeHandler {

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Oauth2AuthorizationRequest authorizationRequest) {
        return Mono.just(
                authorizationRequest instanceof ImplicitOauth2AuthorizationRequest
        );
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest>> permissionGranted(@NotNull Oauth2AuthorizationRequest authorizationRequest,
                                                                                                     @NotNull ResourceOwner resourceOwner) {

        ImplicitOauth2AuthorizationRequest implicitOauth2AuthorizationRequest = (ImplicitOauth2AuthorizationRequest) authorizationRequest;

        return Mono.just(
                ImplicitOauth2AuthorizationResponse.withAssociatedRequest(implicitOauth2AuthorizationRequest)
                        .accessToken("hello")
                        .tokenType(BEARER_TOKEN_TYPE)
                        .expiresIn(3600L)
                        .state(implicitOauth2AuthorizationRequest.getState())
                        .build()
        );
    }
}
