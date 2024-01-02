package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
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
    private ServerSecurityContextRepository securityContextRepository = new WebSessionServerSecurityContextRepository();

    private final Logger logger = LoggerFactory.getLogger(HandlerResourceOwnerAuthenticator.class);

    public HandlerResourceOwnerAuthenticator(ResourceOwnerAuthenticationManager authenticationManager,
                                             ResourceOwnerAuthenticationSuccessHandler successHandler,
                                             ResourceOwnerAuthenticationFailureHandler failureHandler) {

        this.authenticationManager = authenticationManager;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    public HandlerResourceOwnerAuthenticator(ResourceOwnerAuthenticationManager authenticationManager,
                                             ResourceOwnerAuthenticationSuccessHandler successHandler,
                                             ResourceOwnerAuthenticationFailureHandler failureHandler,
                                             ServerSecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    @NotNull
    public Mono<ServerHttpResponse> authenticate(@NotNull ServerWebExchange webExchange) {
        return authenticationManager.attemptAuthentication(webExchange)
                .log()
                .flatMap(authentication -> onAuthenticationSuccess(webExchange, authentication))
                .thenReturn(webExchange.getResponse())
                .onErrorResume(ResourceOwnerAuthenticationException.class,
                        error -> failureHandler.onAuthenticationFailure(webExchange, error)
                                .thenReturn(webExchange.getResponse()));
    }

    @NotNull
    protected Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange webExchange,
                                                 @NotNull AuthenticatedResourceOwnerAuthentication authentication) {
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return securityContextRepository.save(webExchange, securityContext)
                .thenReturn(securityContext)
                .doOnNext(unused -> logger.info("Saved the authentication to: {}", securityContextRepository))
                .thenReturn(authentication)
                .flatMap(auth -> successHandler.onAuthenticationSuccess(webExchange, auth));
    }
}
