package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implementation that just convert {@link ServerWebExchange} to {@link ConsentDecision} and based on
 * that invoke the {@link Oauth2ConsentApprovedHandler} or {@link Oauth2ConsentDeniedHandler}
 */
public class DelegatingOauth2ConsentSubmissionHandler implements Oauth2ConsentSubmissionHandler {

    private final ConsentDecisionResolver consentDecisionResolver;
    private final Oauth2ConsentApprovedHandler approvedHandler;
    private final Oauth2ConsentDeniedHandler oauth2ConsentDeniedHandler;

    public DelegatingOauth2ConsentSubmissionHandler(ConsentDecisionResolver consentDecisionResolver,
                                                    Oauth2ConsentApprovedHandler approvedHandler,
                                                    Oauth2ConsentDeniedHandler oauth2ConsentDeniedHandler) {
        this.consentDecisionResolver = consentDecisionResolver;
        this.approvedHandler = approvedHandler;
        this.oauth2ConsentDeniedHandler = oauth2ConsentDeniedHandler;
    }

    @Override
    @NotNull
    public Mono<Void> handleConsentSubmission(@NotNull Oauth2AuthorizationRequest oauth2AuthorizationRequest,
                                              @NotNull ResourceOwner resourceOwner,
                                              @NotNull ServerWebExchange exchange) {
        return consentDecisionResolver.resolveConsentDecision(exchange)
                .flatMap(consentDecision -> {
                    if ( consentDecision.decision() == ConsentDecision.Decision.APPROVED ) {
                        return approvedHandler.onConsentApproved(oauth2AuthorizationRequest, resourceOwner, consentDecision, exchange);
                    }
                    return oauth2ConsentDeniedHandler.onConsentDenied(oauth2AuthorizationRequest, resourceOwner, consentDecision, exchange);
                });
    }
}
