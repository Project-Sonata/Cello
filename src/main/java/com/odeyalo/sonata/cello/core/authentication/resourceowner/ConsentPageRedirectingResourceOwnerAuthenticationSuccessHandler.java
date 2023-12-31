package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Redirect to consent page after successful resource owner authentication
 */
public class ConsentPageRedirectingResourceOwnerAuthenticationSuccessHandler implements ResourceOwnerAuthenticationSuccessHandler {
    private final static URI CONSENT_PAGE_URI = URI.create("/oauth2/consent");
    private final ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

    @Override
    @NotNull
    public Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange exchange,
                                              @NotNull AuthenticatedResourceOwnerAuthentication authentication) {

        exchange.getResponse().addCookie(ResponseCookie.from("clid", "odeyalo").build());
        return redirectStrategy.sendRedirect(exchange, CONSENT_PAGE_URI);
    }
}
