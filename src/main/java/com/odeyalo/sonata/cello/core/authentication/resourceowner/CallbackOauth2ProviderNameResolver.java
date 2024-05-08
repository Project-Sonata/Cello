package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Contract for resolving the OAuth 2.0 provider name from the callback URI during resource owner authentication.
 * Callback happens when a user is approved OAuth 2.0 consent or deny it
 */
public interface CallbackOauth2ProviderNameResolver {

    /**
     * Resolves the OAuth 2.0 provider name from the callback URI during authentication.
     *
     * @param exchange The ServerWebExchange object representing the HTTP request-response exchange.
     * @return A Mono emitting the OAuth 2.0 provider name as a String.
     */
    @NotNull
    Mono<String> resolveProviderName(@NotNull ServerWebExchange exchange);

}
