package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Provide the URI to redirect the resource owner to authenticate the RO using Oauth2 Provider
 */
public interface Oauth2ProviderRedirectUriProviderManager {
    /**
     * Build ready-to-use redirect uri for the given provider, the resource owner will be redirected to this uri to enter its credentials
     *
     * @param providerName - provider name the resource owner wants to use
     * @return - {@link Mono} with {@link URI} to redirect RO to, empty {@link Mono} if not supported
     */
    @NotNull
    Mono<URI> getProviderRedirectUri(@NotNull String providerName);
}
