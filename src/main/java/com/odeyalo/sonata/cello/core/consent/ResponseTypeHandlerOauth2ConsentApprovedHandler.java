package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handle the response type of this {@link Oauth2AuthorizationRequest} and return the authorization code or access token to Oauth2 client.
 *
 * @author odeyalooo
 */
public class ResponseTypeHandlerOauth2ConsentApprovedHandler implements Oauth2ConsentApprovedHandler {
    private final Oauth2ResponseTypeHandler oauth2ResponseTypeHandler;
    private final Oauth2AuthorizationResponseConverter oauth2AuthorizationResponseConverter;

    public ResponseTypeHandlerOauth2ConsentApprovedHandler(Oauth2ResponseTypeHandler oauth2ResponseTypeHandler,
                                                           Oauth2AuthorizationResponseConverter oauth2AuthorizationResponseConverter) {
        this.oauth2ResponseTypeHandler = oauth2ResponseTypeHandler;
        this.oauth2AuthorizationResponseConverter = oauth2AuthorizationResponseConverter;
    }

    @Override
    @NotNull
    public Mono<Void> onConsentApproved(@NotNull Oauth2AuthorizationRequest authorizationRequest,
                                        @NotNull ConsentDecision consentDecision,
                                        @NotNull ServerWebExchange exchange) {

        return oauth2ResponseTypeHandler.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .flatMap(response -> oauth2AuthorizationResponseConverter.convert(response, exchange))
                .then();
    }
}
