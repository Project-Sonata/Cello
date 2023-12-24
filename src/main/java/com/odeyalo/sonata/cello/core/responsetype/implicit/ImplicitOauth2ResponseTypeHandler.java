package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.*;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerationContext;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * Handle only implicit response type as defined in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2">Implicit Grant type</a>
 */
@Component
public class ImplicitOauth2ResponseTypeHandler implements Oauth2ResponseTypeHandler {

    private final Oauth2AccessTokenGenerator accessTokenGenerator;

    public ImplicitOauth2ResponseTypeHandler(Oauth2AccessTokenGenerator accessTokenGenerator) {
        this.accessTokenGenerator = accessTokenGenerator;
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

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .scopes(implicitRequest.getScopes())
                .client(
                        Oauth2RegisteredClient.builder()
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .credentials(
                                        Oauth2ClientCredentials.withId((implicitRequest.getClientId())))
                                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                                .clientType(ClientType.PUBLIC)
                                .build()) // TODO: Mocked now, should rewrite it
                .resourceOwner(resourceOwner)
                .build();

        return accessTokenGenerator.generateToken(context)
                .map(token -> ImplicitOauth2AuthorizationResponse.withAssociatedRequest(implicitRequest)
                        .accessToken(token.getTokenValue())
                        .tokenType(token.getTokenType().typeName())
                        .scope(token.getScopes())
                        .state(implicitRequest.getState())
                        .expiresIn(token.remainingLifetime().getSeconds())
                        .build()
        );
    }
}
