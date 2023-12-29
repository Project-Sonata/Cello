package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * {@link Oauth2AuthorizationRequestRepository} implementation that saves the {@link Oauth2AuthorizationRequest} in {@link org.springframework.web.server.WebSession}
 */
public class WebSessionOauth2AuthorizationRequestRepository implements Oauth2AuthorizationRequestRepository {
    private static final String AUTHORIZATION_REQUEST_KEY = "authorization_request";

    @Override
    @NotNull
    public Mono<Void> saveAuthorizationRequest(@NotNull Oauth2AuthorizationRequest request, @NotNull ServerWebExchange httpExchange) {
        return httpExchange.getSession()
                        .doOnNext(session -> session.getAttributes().put(AUTHORIZATION_REQUEST_KEY, request))
                        .then();
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationRequest> loadAuthorizationRequest(@NotNull ServerWebExchange httpExchange) {
        return httpExchange.getSession()
                .mapNotNull(session -> session.getAttribute(AUTHORIZATION_REQUEST_KEY))
                .cast(Oauth2AuthorizationRequest.class);
    }

    @Override
    @NotNull
    public Mono<Void> removeAuthorizationRequest(@NotNull ServerWebExchange httpExchange) {
        return httpExchange.getSession()
                .doOnNext(session -> session.getAttributes().remove(AUTHORIZATION_REQUEST_KEY))
                .then();
    }
}