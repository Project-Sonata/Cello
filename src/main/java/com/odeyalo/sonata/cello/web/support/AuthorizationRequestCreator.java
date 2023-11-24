package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Interface to create the {@link AuthorizationRequest} from {@link ServerWebExchange}
 */
public interface AuthorizationRequestCreator {
    /**
     * Create and return the {@link AuthorizationRequest} if success
     * @param exchange - exchange to create {@link AuthorizationRequest} from
     * @return - created {@link AuthorizationRequest}
     * @throws AuthorizationRequestCreationException - if {@link AuthorizationRequest} cannot be created
     */
    @NotNull
    Mono<AuthorizationRequest> createAuthorizationRequest(@NotNull ServerWebExchange exchange);

}
