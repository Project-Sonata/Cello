package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handle the approved consent by the resource owner
 *
 * @see RequestCacheRedirectingOauth2ConsentApprovedHandler
 */
public interface Oauth2ConsentApprovedHandler {
    /**
     * Invoked when the resource owner approved consent
     * @param authorizationRequest - current authorization request
     * @param consentDecision - decision made by the resource owner
     * @param exchange - current http exchange
     * @return - {@link Mono} with {@link Void}. Response should be written in {@link ServerWebExchange#getResponse()}
     */
    @NotNull
    Mono<Void> onConsentApproved(@NotNull Oauth2AuthorizationRequest authorizationRequest,
                                 @NotNull ConsentDecision consentDecision,
                                 @NotNull ServerWebExchange exchange);

}
