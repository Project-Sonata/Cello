package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Authenticate the user in any possible way.
 * Default implementation is {@link UsernamePasswordResourceOwnerAuthenticationManager}
 */
public interface ResourceOwnerAuthenticationManager {

    /**
     * Attempts the authentication of the resource owner using the given {@link ServerWebExchange}
     *
     * @param webExchange - current web exchange with request and response
     * @return Mono with {@link AuthenticatedResourceOwnerAuthentication} on success,
     * empty mono if implementation does not support this type of the authentication,
     * Mono with {@link ResourceOwnerAuthenticationException} error if invalid credentials is used
      */

    @NotNull
    Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull ServerWebExchange webExchange);

}
