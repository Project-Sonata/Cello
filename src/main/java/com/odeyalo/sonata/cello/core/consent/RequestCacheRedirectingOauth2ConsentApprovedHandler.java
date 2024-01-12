package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Redirect the resource owner to saved request in {@link ServerRequestCache}
 */
public class RequestCacheRedirectingOauth2ConsentApprovedHandler implements Oauth2ConsentApprovedHandler {
    private final ServerRedirectStrategy serverRedirectStrategy = new DefaultServerRedirectStrategy();
    private final ServerRequestCache serverRequestCache;

    public RequestCacheRedirectingOauth2ConsentApprovedHandler(ServerRequestCache serverRequestCache) {
        this.serverRequestCache = serverRequestCache;
    }

    @Override
    @NotNull
    public Mono<Void> onConsentApproved(@NotNull Oauth2AuthorizationRequest currentRequest,
                                        @NotNull ResourceOwner resourceOwner,
                                        @NotNull ConsentDecision consentDecision,
                                        @NotNull ServerWebExchange exchange) {

        return serverRequestCache.getRedirectUri(exchange)
                .flatMap(uri -> serverRedirectStrategy.sendRedirect(exchange, uri));
    }
}
