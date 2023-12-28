package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Responsible to save the {@link Oauth2AuthorizationRequest} to access it between different requests
 */
public interface AuthorizationRequestRepository {

    @NotNull
    Mono<Void> saveAuthorizationRequest(@NotNull Oauth2AuthorizationRequest request,
                                        @NotNull ServerWebExchange httpExchange);

    /**
     * Load the {@link Oauth2AuthorizationRequest} from the given {@link ServerWebExchange}
     * @param httpExchange - http exchange to get {@link Oauth2AuthorizationRequest} from
     * @return - {@link Mono} populated with {@link Oauth2AuthorizationRequest} if exists,
     * empty {@link Mono} if not
     */
    @NotNull
    Mono<Oauth2AuthorizationRequest> loadAuthorizationRequest(@NotNull ServerWebExchange httpExchange);

    /**
     * Remove the {@link Oauth2AuthorizationRequest} from repository
     * @param httpExchange - exchange to delete {@link Oauth2AuthorizationRequest} from
     * @return - {@link Mono} with {@link Void}.
     */
    @NotNull
    Mono<Void> removeAuthorizationRequest(@NotNull ServerWebExchange httpExchange);

}
