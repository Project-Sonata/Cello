package com.odeyalo.sonata.cello.spring.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CelloOauth2AuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {


        if ( !(authentication instanceof CelloOauth2CookieResourceOwnerAuthentication authenticationToken) ) {
            return Mono.empty();
        }

        authenticationToken.setAuthenticated(true);

        return Mono.just(authentication);
    }
}
