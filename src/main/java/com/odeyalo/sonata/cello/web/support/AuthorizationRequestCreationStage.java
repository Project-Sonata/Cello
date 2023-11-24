package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Support interface to build the {@link AuthorizationRequest} in separated steps
 */
public interface AuthorizationRequestCreationStage {
    /**
     * Construct the AuthorizationRequest based on ServerWebExchange.
     *
     * @param exchange - exchange to build AuthorizationRequest from
     * @param prev     - previously processed Builder from another AuthorizationRequestCreatorSupport
     * @return updated AuthorizationRequestBuilder or the same as provided
     * @throws AuthorizationRequestCreationException - if the exchange does not contain required data
     * or AuthorizationRequest cannot be created by any other reason
     */
    @NotNull
    Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                      @NotNull AuthorizationRequestBuilder prev);
}
