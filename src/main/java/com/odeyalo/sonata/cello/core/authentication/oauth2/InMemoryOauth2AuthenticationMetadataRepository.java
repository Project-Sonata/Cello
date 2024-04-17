package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@link Oauth2AuthenticationMetadata} that stores values in {@link ConcurrentHashMap}
 */
@Component
public final class InMemoryOauth2AuthenticationMetadataRepository implements Oauth2AuthenticationMetadataRepository {
    private final Map<String, Oauth2AuthenticationMetadata> cache = new ConcurrentHashMap<>();

    @Override
    @NotNull
    public Mono<Void> save(@NotNull String id,
                           @NotNull Oauth2AuthenticationMetadata metadata) {
        return Mono.fromRunnable(
                () -> cache.put(id, metadata)
        );
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthenticationMetadata> findBy(@NotNull String id) {
        return Mono.justOrEmpty(
                cache.get(id)
        );
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthenticationMetadata> remove(@NotNull String id) {
        return Mono.justOrEmpty(
                cache.remove(id)
        );
    }
}
