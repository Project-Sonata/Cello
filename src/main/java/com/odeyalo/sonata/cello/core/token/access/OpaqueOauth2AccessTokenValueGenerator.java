package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Generate unique value for the {@link Oauth2AccessToken}
 */
public interface OpaqueOauth2AccessTokenValueGenerator {

    /**
     * Generate unique value for the {@link Oauth2AccessToken}
     * @return - value that should be set in {@link Oauth2AccessToken}
     */
    @NotNull
    Mono<String> generateTokenValue();

}
