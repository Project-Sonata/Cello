package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Generate UUID value for the {@link Oauth2AccessToken}
 */
@Component
public class UUIDOpaqueOauth2AccessTokenValueGenerator implements OpaqueOauth2AccessTokenValueGenerator {

    @Override
    public @NotNull Mono<String> generateTokenValue() {
        return Mono.just(UUID.randomUUID().toString());
    }
}
