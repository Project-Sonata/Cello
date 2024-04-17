package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Generate redirect uri to which user will be redirected to authenticate itself using third-party oauth2 provider.
 */
public interface Oauth2ProviderRedirectUriGenerator {
    /**
     * Generates and returns a {@link URI} to which resource owner will be redirected to
     * @param providerName - name of the provider that resource owner wants to use
     * @param context - a context that MUST be used while generating a redirect uri, in most cases it must be appended as query parameters in redirect uri
     * @return {@link Mono} populated with {@link URI} to redirect user to.
     * @throws NotSupportedOauth2ProviderException - if the provider does not supported by Cello
     */
    @NotNull
    Mono<URI> generateOauth2RedirectUri(@NotNull String providerName,
                                        @NotNull Oauth2AuthenticationRedirectUriGenerationContext context);
}
