package com.odeyalo.sonata.cello.core.authentication;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface ResourceOwnerAuthenticationManager {

    @NotNull
    Mono<AuthenticatedResourceOwner> attemptAuthentication(@NotNull ResourceOwnerPreAuthentication preAuthentication);



}

//
//ResourceOwnerAuthenticationFilter -> {
//    convertAuthneitcation(exchange) -> authManager.auth(auth) ->
//        success ? authenticationSuccessHandler(exchange, authentication)
//        : authenticationFailureHandler(exchange, exception)
//}