package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In memory implementation of {@link AuthorizationCodeRepository} that stores {@link GeneratedAuthorizationCode} in {@link ConcurrentMap}
 */
public final class InMemoryAuthorizationCodeRepository implements AuthorizationCodeRepository {
    private final ConcurrentMap<String, GeneratedAuthorizationCode> cache;

    public InMemoryAuthorizationCodeRepository() {
        this.cache = new ConcurrentHashMap<>();
    }

    public InMemoryAuthorizationCodeRepository(final Map<String, GeneratedAuthorizationCode> cache) {
        this.cache = new ConcurrentHashMap<>(cache);
    }

    @Override
    @NotNull
    public Mono<Void> save(@NotNull final GeneratedAuthorizationCode authorizationCode) {
        return Mono.fromRunnable(() -> cache.put(authorizationCode.getCodeValue(), authorizationCode));
    }

    @Override
    @NotNull
    public Mono<GeneratedAuthorizationCode> findByCodeValue(@NotNull final String authorizationCodeValue) {
        return Mono.justOrEmpty(
                cache.get(authorizationCodeValue)
        );
    }

    @Override
    @NotNull
    public Mono<Void> deleteByCodeValue(@NotNull final String authorizationCodeValue) {
        return Mono.fromRunnable(() -> cache.remove(authorizationCodeValue));
    }
}
