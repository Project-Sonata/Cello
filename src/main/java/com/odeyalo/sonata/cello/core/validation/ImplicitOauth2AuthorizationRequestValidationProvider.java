package com.odeyalo.sonata.cello.core.validation;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Handle validation of {@link ImplicitOauth2AuthorizationRequest} only.
 */
@Component
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
                .switchIfEmpty(
                        Mono.error(
                                Oauth2AuthorizationRequestValidationException.errorCodeOnly(Oauth2ErrorCode.INVALID_CLIENT)
                        )
                ).then();
    }
}
