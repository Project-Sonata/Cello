package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * An implementation of {@link Oauth2StateGenerator} that just generates a UUID strings
 */
public final class UUIDOauth2StateGenerator implements Oauth2StateGenerator {

    @Override
    public @NotNull Mono<String> generateState() {
        return Mono.just(
                UUID.randomUUID().toString()
        );
    }
}
