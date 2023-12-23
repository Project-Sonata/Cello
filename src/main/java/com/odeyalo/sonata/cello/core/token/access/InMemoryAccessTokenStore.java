package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link AccessTokenStore} that store the {@link AccessToken} in {@link ConcurrentMap}
 */
@Component
public class InMemoryAccessTokenStore implements AccessTokenStore {
    public final ConcurrentMap<String, AccessToken> tokens = new ConcurrentHashMap<>();

    @Override
    @NotNull
    public Mono<Void> saveToken(@NotNull AccessToken accessToken) {
        return Mono.fromRunnable(() ->
                tokens.put(accessToken.getTokenValue(), accessToken)
        );
    }

    @Override
    @NotNull
    public Mono<AccessToken> findTokenByTokenValue(@NotNull String tokenValue) {
        return Mono.justOrEmpty(tokens.get(tokenValue));
    }

    @Override
    @NotNull
    public Mono<Boolean> exists(@NotNull String tokenValue) {
        return Mono.just(tokens.containsKey(tokenValue));
    }
}
