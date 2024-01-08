package com.odeyalo.sonata.cello.core;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;

/**
 * {@link Oauth2AuthorizationRequestRepository} implementation that saves the {@link Oauth2AuthorizationRequest} in {@link org.springframework.web.server.WebSession}
 *
 * <p>
 * The implementation stores the {@link java.util.Map} of {@link Oauth2AuthorizationRequest} in {@link WebSession#getAttributes()}
 * with {@link #AUTHORIZATION_REQUESTS_KEY}, different clients and flows has different IDs, so {@link java.util.Map} has {@link String} type as key(current flow id)
 * and {@link Oauth2AuthorizationRequest} as value
 * </p>
 */
@Log4j2
public class WebSessionOauth2AuthorizationRequestRepository implements Oauth2AuthorizationRequestRepository {
    private static final String AUTHORIZATION_REQUESTS_KEY = "authorization_request";

    @Override
    @NotNull
    public Mono<Void> saveAuthorizationRequest(@NotNull Oauth2AuthorizationRequest authorizationRequest, @NotNull ServerWebExchange httpExchange) {
        String currentFlowId = httpExchange.getAttribute(CURRENT_FLOW_ATTRIBUTE_NAME);

        if ( currentFlowId == null ) {
            return missingFlowAttributeError();
        }

        return httpExchange.getSession()
                .doOnNext(session -> associateAuthorizationRequestWithFlow(authorizationRequest, currentFlowId, session))
                .doOnNext(unused -> log.info("Saved authorization request to http session."))
                .then();
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationRequest> loadAuthorizationRequest(@NotNull ServerWebExchange httpExchange) {
        String currentFlowId = httpExchange.getAttribute(CURRENT_FLOW_ATTRIBUTE_NAME);

        if ( currentFlowId == null ) {
            return missingFlowAttributeError();
        }

        return httpExchange.getSession()
                .map(session -> session.getAttributeOrDefault(AUTHORIZATION_REQUESTS_KEY, new HashMap<String, Oauth2AuthorizationRequest>()))
                .mapNotNull(authorizationRequests -> authorizationRequests.get(currentFlowId))
                .cast(Oauth2AuthorizationRequest.class)
                .doOnNext(request -> log.debug("Successfully retrieve {} from http session", request));
    }

    @Override
    @NotNull
    public Mono<Void> removeAuthorizationRequest(@NotNull ServerWebExchange httpExchange) {
        String currentFlowId = httpExchange.getAttribute(CURRENT_FLOW_ATTRIBUTE_NAME);

        if ( currentFlowId == null ) {
            return missingFlowAttributeError();
        }

        return httpExchange.getSession()
                .map(session -> session.getAttributeOrDefault(AUTHORIZATION_REQUESTS_KEY, new HashMap<String, Oauth2AuthorizationRequest>()))
                .doOnNext(authorizationRequests -> authorizationRequests.remove(currentFlowId))
                .doOnNext(unused -> log.debug("Removed Oauth2AuthorizationRequest from http session."))
                .then();
    }

    private static void associateAuthorizationRequestWithFlow(@NotNull Oauth2AuthorizationRequest authorizationRequest, String currentFlowId, WebSession session) {
        HashMap<String, Oauth2AuthorizationRequest> authorizationRequests = session.getAttributeOrDefault(AUTHORIZATION_REQUESTS_KEY, new HashMap<>());
        session.getAttributes().putIfAbsent(AUTHORIZATION_REQUESTS_KEY, authorizationRequests);
        authorizationRequests.put(currentFlowId, authorizationRequest);
    }

    @NotNull
    private static <T> Mono<T> missingFlowAttributeError() {
        return Mono.error(
                new IllegalStateException("Missing {" + CURRENT_FLOW_ATTRIBUTE_NAME + "} attribute")
        );
    }
}