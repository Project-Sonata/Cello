package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Contract to exchange the {@link AuthorizationCode} to access token that then wrapped in {@link Oauth2AccessTokenResponse}.
 * @see DefaultAuthorizationCodeExchange
 */
public interface AuthorizationCodeExchange {
    /**
     * Exchange the given {@link AuthorizationCode} to {@link Oauth2AccessTokenResponse}
     * @param code authorization code that used to be exchanged to access token
     * @return {@link Mono} emitting {@link Oauth2AccessTokenResponse} on success, or {@link Mono#error(Throwable)} with {@link AuthorizationCodeExchangeException}
     *
     * @throws AuthorizationCodeExchangeException if authorization code can't be exchanged for access token
     */
    @NotNull
    Mono<Oauth2AccessTokenResponse> exchange(@NotNull AuthorizationCode code);

    /**
     * Used to determine if this implementation supports this type of OAuth 2.0 provider
     * @param providerName - name of the provider(google, GitHub)
     *
     * @return {@code true} if provider is supported by this implementation, {@code false} otherwise
     *
     * @see Oauth2ProviderRegistration#getName() for further info about provider name
     */
    boolean supports(String providerName);

}
