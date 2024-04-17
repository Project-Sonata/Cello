package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * A generator to generate unique state that will be appended while sending oauth2 authorization request to oauth2 third-party provider
 */
public interface Oauth2StateGenerator {
    /**
     * @return a unique string identifier of oauth2 state
     */
    @NotNull
    Mono<String> generateState();

}
