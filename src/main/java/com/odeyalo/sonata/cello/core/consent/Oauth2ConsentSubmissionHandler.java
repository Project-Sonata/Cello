package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handle the submission of the oauth2 consent page
 */
public interface Oauth2ConsentSubmissionHandler {
    /**
     * Handle the submission of consent by the resource owner
     * @param authorizationRequest - current authorization request
     * @param resourceOwner  - resource owner that submitted consent decision
     * @param exchange - current http exchange
     * @return - {@link Mono} with {@link Void}
     */
    @NotNull
    Mono<Void> handleConsentSubmission(@NotNull Oauth2AuthorizationRequest authorizationRequest,
                                       @NotNull ResourceOwner resourceOwner,
                                       @NotNull ServerWebExchange exchange);

}
