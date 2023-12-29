package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Redirect the resource owner to saved URI in {@link ServerRequestCache} after successful authentication
 */
public class RedirectingResourceOwnerAuthenticationSuccessHandler implements ResourceOwnerAuthenticationSuccessHandler {
    private final ServerRequestCache serverRequestCache;
    private final URI DEFAULT_REDIRECT_URI = URI.create("/");

    public RedirectingResourceOwnerAuthenticationSuccessHandler(ServerRequestCache serverRequestCache) {
        this.serverRequestCache = serverRequestCache;
    }

    @Override
    @NotNull
    public Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange exchange,
                                              @NotNull AuthenticatedResourceOwnerAuthentication authentication) {

        return serverRequestCache.getRedirectUri(exchange)
                .defaultIfEmpty(DEFAULT_REDIRECT_URI)
                .doOnNext(uri -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                    exchange.getResponse().getHeaders().setLocation(uri);
                }).then();
    }
}
