package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Responsible to save the {@link Oauth2AuthorizationRequest} to access it between different requests.
 *
 * <p>
 * To make the {@link Oauth2AuthorizationRequestRepository} store multiple {@link Oauth2AuthorizationRequest}s,
 * every {@link ServerWebExchange} MUST contain the {@link #CURRENT_FLOW_ATTRIBUTE_NAME} in {@link ServerWebExchange#getAttributes()},
 * </p>
 *
 * @see WebSessionOauth2AuthorizationRequestRepository
 */
public interface Oauth2AuthorizationRequestRepository {
    /**
     * Key for attribute that should be present in every request
     */
    String CURRENT_FLOW_ATTRIBUTE_NAME = "flow_id";

    /**
     * Saves the {@link Oauth2AuthorizationRequest},
     * associate the given {@link Oauth2AuthorizationRequest} with presented {@link #CURRENT_FLOW_ATTRIBUTE_NAME} in request attributes
     * If the {@link Oauth2AuthorizationRequest} already associated with {@link #CURRENT_FLOW_ATTRIBUTE_NAME} old value MUST be overriden
     * @param request - oauth2 authorization request to save
     * @param httpExchange - current http exchange, that MUST contain {@link #CURRENT_FLOW_ATTRIBUTE_NAME} in request attributes
     * @return - {@link Mono} with {@link Mono} on success, {@link Mono} with error if {@link Oauth2AuthorizationRequest} cannot be saved
     *
     * @throws IllegalStateException - if the {@link ServerWebExchange#getAttributes()} does not contain {@link #CURRENT_FLOW_ATTRIBUTE_NAME}
     */
    @NotNull
    Mono<Void> saveAuthorizationRequest(@NotNull Oauth2AuthorizationRequest request,
                                        @NotNull ServerWebExchange httpExchange);

    /**
     * Load the {@link Oauth2AuthorizationRequest} from the given {@link ServerWebExchange}
     * Note, that {@link ServerWebExchange#getAttributes()} MUST contain the {@link #CURRENT_FLOW_ATTRIBUTE_NAME} attribute,
     * that will be used to differ {@link Oauth2AuthorizationRequest} from each other
     * @param httpExchange - http exchange to get {@link Oauth2AuthorizationRequest} from
     * @return - {@link Mono} populated with {@link Oauth2AuthorizationRequest} if exists,
     * empty {@link Mono} if not
     */
    @NotNull
    Mono<Oauth2AuthorizationRequest> loadAuthorizationRequest(@NotNull ServerWebExchange httpExchange);

    /**
     * Remove the {@link Oauth2AuthorizationRequest} from repository, based on {@link #CURRENT_FLOW_ATTRIBUTE_NAME} value
     *
     * @param httpExchange - exchange to delete {@link Oauth2AuthorizationRequest} from
     * @return - {@link Mono} with {@link Void}.
     */
    @NotNull
    Mono<Void> removeAuthorizationRequest(@NotNull ServerWebExchange httpExchange);

}
