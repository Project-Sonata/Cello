package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * A simple implementation that always returns static value as result that was predefined in constructor.
 * It is not recommended for production environment
 */
public final class StaticCallbackOauth2ProviderNameResolver implements CallbackOauth2ProviderNameResolver {
    private final String providerName;

     StaticCallbackOauth2ProviderNameResolver(String providerName) {
        this.providerName = providerName;
    }

    @NotNull
    public static StaticCallbackOauth2ProviderNameResolver alwaysReturn(@NotNull String providerName) {
        return new StaticCallbackOauth2ProviderNameResolver(providerName);
    }

    @Override
    @NotNull
    public Mono<String> resolveProviderName(@NotNull ServerWebExchange exchange) {
        return Mono.just(providerName);
    }
}
