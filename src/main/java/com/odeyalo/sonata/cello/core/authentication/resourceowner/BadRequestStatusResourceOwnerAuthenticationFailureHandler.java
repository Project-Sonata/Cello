package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Return the bad request as status to resource owner
 */
public class BadRequestStatusResourceOwnerAuthenticationFailureHandler implements ResourceOwnerAuthenticationFailureHandler {

    @Override
    @NotNull
    public Mono<Void> onAuthenticationFailure(@NotNull ServerWebExchange exchange, @NotNull ResourceOwnerAuthenticationException authenticationException) {
        return Mono.fromRunnable(
                () -> exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST)
        );
    }
}
