package com.odeyalo.sonata.cello.core.authentication;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UsernamePasswordResourceOwnerAuthenticationManager implements ResourceOwnerAuthenticationManager {

    @Override
    public @NotNull Mono<AuthenticatedResourceOwner> attemptAuthentication(@NotNull ResourceOwnerPreAuthentication preAuthentication) {
        return Mono.just(AuthenticatedResourceOwner.of(preAuthentication.getPrincipal(), preAuthentication.getCredentials(), null));
    }
}
