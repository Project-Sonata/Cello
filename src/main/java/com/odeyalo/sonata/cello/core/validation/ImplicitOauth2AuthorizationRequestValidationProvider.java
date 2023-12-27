package com.odeyalo.sonata.cello.core.validation;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.core.RedirectUris;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import com.odeyalo.sonata.cello.exception.UnacceptableOauth2RedirectUriException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handle validation of {@link ImplicitOauth2AuthorizationRequest} only.
 */
public class ImplicitOauth2AuthorizationRequestValidationProvider implements Oauth2AuthorizationRequestValidationProvider {
    private final Oauth2RegisteredClientService clientService;

    @Autowired
    public ImplicitOauth2AuthorizationRequestValidationProvider(Oauth2RegisteredClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Oauth2AuthorizationRequest request) {
        return Mono.just(
                request instanceof ImplicitOauth2AuthorizationRequest
        );
    }

    @Override
    @NotNull
    public Mono<Void> validate(@NotNull Oauth2AuthorizationRequest request) {
        ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;

        return clientService.findByClientId(implicitRequest.getClientId())
                .flatMap(client -> validateAllowedUri(implicitRequest, client))
                .switchIfEmpty(
                        Mono.error(
                                Oauth2AuthorizationRequestValidationException.errorCodeOnly(Oauth2ErrorCode.INVALID_CLIENT)
                        )
                ).then();
    }

    @NotNull
    private static Mono<Oauth2RegisteredClient> validateAllowedUri(ImplicitOauth2AuthorizationRequest implicitRequest, Oauth2RegisteredClient client) {
        RedirectUris allowedRedirectUris = client.getAllowedRedirectUris();
        boolean isAllowed = allowedRedirectUris.contains(implicitRequest.getRedirectUri());
        if ( isAllowed ) {
            return Mono.just(client);
        }
        return Mono.error(
                UnacceptableOauth2RedirectUriException.defaultException()
        );
    }
}
