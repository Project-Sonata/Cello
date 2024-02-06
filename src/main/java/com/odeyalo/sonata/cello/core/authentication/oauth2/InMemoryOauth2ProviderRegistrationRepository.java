package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation that stores the values in {@link ConcurrentHashMap}
 */
public final class InMemoryOauth2ProviderRegistrationRepository implements Oauth2ProviderRegistrationRepository {
    private final Map<String, Oauth2ProviderRegistration> cache;

    public InMemoryOauth2ProviderRegistrationRepository() {
        this.cache = new ConcurrentHashMap<>();
    }

    public InMemoryOauth2ProviderRegistrationRepository(Map<String, Oauth2ProviderRegistration> initProviders) {
        this.cache = new ConcurrentHashMap<>(initProviders);
    }

    @Override
    @NotNull
    public Mono<Oauth2ProviderRegistration> save(@NotNull String providerName, @NotNull Oauth2ProviderRegistration registration) {
        return Mono.justOrEmpty(cache.put(providerName, registration))
                .thenReturn(registration);
    }

    @Override
    @NotNull
    public Mono<Oauth2ProviderRegistration> findByProviderName(@NotNull String providerName) {
        return Mono.justOrEmpty(cache.get(providerName));
    }

    @Override
    @NotNull
    public Mono<Void> removeByProviderName(@NotNull String providerName) {
        return Mono.fromRunnable(
                () -> cache.remove(providerName)
        );
    }
}
