package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerationContext;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;


/**
 * Handle only implicit response type as defined in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2">Implicit Grant type</a>
 */
public class ImplicitOauth2ResponseTypeHandler implements Oauth2ResponseTypeHandler {

    private final Oauth2AccessTokenGenerator accessTokenGenerator;
    private final Oauth2RegisteredClientService clientService;

    public ImplicitOauth2ResponseTypeHandler(Oauth2AccessTokenGenerator accessTokenGenerator,
                                             Oauth2RegisteredClientService clientService) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.clientService = clientService;
    }

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

        ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) authorizationRequest;

        return clientService.findByClientId(implicitRequest.getClientId())
                .map(client -> createTokenContext(resourceOwner, implicitRequest, client))
                .flatMap(accessTokenGenerator::generateToken)
                .map(
                        token -> buildImplicitOauth2AuthorizationResponse(implicitRequest, token)
                );
    }

    private static ImplicitOauth2AuthorizationResponse buildImplicitOauth2AuthorizationResponse(ImplicitOauth2AuthorizationRequest implicitRequest, Oauth2AccessToken token) {
        return ImplicitOauth2AuthorizationResponse.withAssociatedRequest(implicitRequest)
                .accessToken(token.getTokenValue())
                .tokenType(token.getTokenType().typeName())
                .scope(token.getScopes())
                .state(implicitRequest.getState())
                .expiresIn(token.remainingLifetime().getSeconds())
                .build();
    }

    private static Oauth2AccessTokenGenerationContext createTokenContext(@NotNull ResourceOwner resourceOwner,
                                                                         @NotNull ImplicitOauth2AuthorizationRequest implicitRequest,
                                                                         @NotNull Oauth2RegisteredClient client) {
        return Oauth2AccessTokenGenerationContext.builder()
                .scopes(implicitRequest.getScopes())
                .client(client)
                .resourceOwner(resourceOwner)
                .build();
    }
}
