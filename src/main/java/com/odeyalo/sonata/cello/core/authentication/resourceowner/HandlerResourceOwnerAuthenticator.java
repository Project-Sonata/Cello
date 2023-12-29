package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implementation that uses the {@link ResourceOwnerAuthenticationFailureHandler} on authentication failure or
 * {@link ResourceOwnerAuthenticationSuccessHandler} on authentication success
 */
public class HandlerResourceOwnerAuthenticator implements ResourceOwnerAuthenticator {

    private final ResourceOwnerAuthenticationManager authenticationManager;
    private final ResourceOwnerAuthenticationSuccessHandler successHandler;
    private final ResourceOwnerAuthenticationFailureHandler failureHandler;

    public HandlerResourceOwnerAuthenticator(ResourceOwnerAuthenticationManager authenticationManager,
                                             ResourceOwnerAuthenticationSuccessHandler successHandler,
                                             ResourceOwnerAuthenticationFailureHandler failureHandler) {

        this.authenticationManager = authenticationManager;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    @NotNull
    public Mono<ServerHttpResponse> authenticate(@NotNull ServerWebExchange webExchange) {
        return authenticationManager.attemptAuthentication(webExchange)
                .flatMap(authentication -> successHandler.onAuthenticationSuccess(webExchange, authentication))
                .thenReturn(webExchange.getResponse())
                .onErrorResume(ResourceOwnerAuthenticationException.class,
                        error -> failureHandler.onAuthenticationFailure(webExchange, error)
                                .thenReturn(webExchange.getResponse()));
    }
}
