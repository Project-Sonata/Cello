package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Default implementation of {@link Oauth2ConsentDeniedHandler} that just redirect resource owner to {@link Oauth2AuthorizationRequest#getRedirectUri()}
 * and appends a 'error' parameter with 'access_denied' to it.
 */
public final class DefaultOauth2ConsentDeniedHandler implements Oauth2ConsentDeniedHandler {
    private final ServerRedirectStrategy serverRedirectStrategy;

    public DefaultOauth2ConsentDeniedHandler() {
        this.serverRedirectStrategy = new DefaultServerRedirectStrategy();
    }

    public DefaultOauth2ConsentDeniedHandler(@NotNull ServerRedirectStrategy serverRedirectStrategy) {
        this.serverRedirectStrategy = serverRedirectStrategy;
    }

    @Override
    @NotNull
    public Mono<Void> onConsentDenied(@NotNull Oauth2AuthorizationRequest request,
                                      @NotNull ResourceOwner resourceOwner,
                                      @NotNull ConsentDecision decision,
                                      @NotNull ServerWebExchange httpExchange) {

        URI errorAwareRedirectUri = UriComponentsBuilder.fromUri(request.getRedirectUri().asUri())
                .queryParam("error", "access_denied")
                .build()
                .toUri();

        return serverRedirectStrategy.sendRedirect(httpExchange, errorAwareRedirectUri);
    }
}
