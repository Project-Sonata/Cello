package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link ServerWebExchange} to specific {@link AuthenticationCredentials}
 */
public interface AuthenticationCredentialsConverter {
    /**
     * Convert the web exchange to {@link AuthenticationCredentials}
     * @param exchange - current http exchange
     * @return - {@link Mono} with resolved {@link AuthenticationCredentials} or empty {@link Mono} if not supported by this implementation
     */
    @NotNull
    Mono<AuthenticationCredentials> convertToCredentials(@NotNull ServerWebExchange exchange);

}
