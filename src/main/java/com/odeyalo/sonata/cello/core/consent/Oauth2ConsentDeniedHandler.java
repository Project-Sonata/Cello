package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handle the denied submission of oauth2 consent by the resource owner.
 */
public interface Oauth2ConsentDeniedHandler {
    /**
     * Invoked when resource owner denied the oauth2 consent.
     * @param request - current authorization request
     * @param resourceOwner - resource owner that denied consent
     * @param decision - decision made by the user
     * @param httpExchange - current http exchange
     * @return - empty {@link Mono}, response is written in {@link ServerWebExchange#getResponse()}
     */
    @NotNull
    Mono<Void> onConsentDenied(@NotNull Oauth2AuthorizationRequest request,
                               @NotNull ResourceOwner resourceOwner,
                               @NotNull ConsentDecision decision,
                               @NotNull ServerWebExchange httpExchange);
}
