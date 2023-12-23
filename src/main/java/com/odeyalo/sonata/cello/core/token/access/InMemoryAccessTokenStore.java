package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link AccessTokenStore} that store the {@link Oauth2AccessToken} in {@link ConcurrentMap}
 */
@Component
public class InMemoryAccessTokenStore implements AccessTokenStore {
    public final ConcurrentMap<String, Oauth2AccessToken> tokens = new ConcurrentHashMap<>();

    @Override
    @NotNull
    public Mono<Void> saveToken(@NotNull Oauth2AccessToken accessToken) {
        return Mono.fromRunnable(() ->
                tokens.put(accessToken.getTokenValue(), accessToken)
        );
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessToken> findTokenByTokenValue(@NotNull String tokenValue) {
        return Mono.justOrEmpty(tokens.get(tokenValue));
    }

    @Override
    @NotNull
    public Mono<Boolean> exists(@NotNull String tokenValue) {
        return Mono.just(tokens.containsKey(tokenValue));
    }
}
