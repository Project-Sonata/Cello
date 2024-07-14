package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Generate a new {@link GeneratedAuthorizationCode} that can be exchanged for access token as described in
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-1.3.1">Authorization code</a>
 */
public interface AuthorizationCodeGenerator {
    /**
     * Generate a new {@link GeneratedAuthorizationCode} that can be exchanged for access token
     * @return = {@link Mono} with {@link GeneratedAuthorizationCode}
     */
    @NotNull
    Mono<GeneratedAuthorizationCode> newAuthorizationCode();

}
