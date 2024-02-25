package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Simple repository to save {@link Oauth2ProviderRegistration} instances
 */
public interface Oauth2ProviderRegistrationRepository {
    /**
     * Save the {@link Oauth2ProviderRegistration} with the given registration name
     *
     * @param providerName -the provider unique name
     * @param registration - {@link Oauth2ProviderRegistration} instance to register
     * @return - registered {@link Oauth2ProviderRegistration} wrapped in {@link Mono}, on existing {@link Oauth2ProviderRegistration} with provider name REWRITE existing one
     */
    @NotNull
    Mono<Oauth2ProviderRegistration> save(@NotNull String providerName, @NotNull Oauth2ProviderRegistration registration);

    /**
     * @param providerName = provider name to search {@link Oauth2ProviderRegistration}
     * @return - {@link Mono} with found {@link Oauth2ProviderRegistration}, empty {@link Mono} otherwise
     */
    @NotNull
    Mono<Oauth2ProviderRegistration> findByProviderName(@NotNull String providerName);

    /**
     * Remove the {@link Oauth2ProviderRegistration} from repository by provider name
     *
     * @param providerName - provider name associated with {@link Oauth2ProviderRegistration}
     * @return - always {@link Mono} with {@link Void}
     */
    @NotNull
    Mono<Void> removeByProviderName(@NotNull String providerName);

}
