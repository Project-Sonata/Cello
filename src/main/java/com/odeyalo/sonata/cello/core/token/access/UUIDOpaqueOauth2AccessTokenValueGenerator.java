package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Generate UUID value for the {@link Oauth2AccessToken}
 */
public class UUIDOpaqueOauth2AccessTokenValueGenerator implements OpaqueOauth2AccessTokenValueGenerator {

    @Override
    @NotNull
    public Mono<String> generateTokenValue() {
        return Mono.just(UUID.randomUUID().toString());
    }
}
