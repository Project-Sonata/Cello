package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Contract to handle the response from OAuth 2.0 provider and convert it to {@link Oauth2AccessTokenResponse}.
 */
public interface Oauth2AccessTokenResponseHandler {
    /**
     * Convert the given {@link WebClient.ResponseSpec} to {@link Oauth2AccessTokenResponse}.
     * @param responseSpec - response received from OAuth 2.0 provider
     * @return - {@link Mono} emitting {@link Oauth2AccessTokenResponse} or {@link Mono#error(Throwable)} with {@link AuthorizationCodeExchangeException}
     *
     * @throws AuthorizationCodeExchangeException if authorization code wasn't successfully exchanged for access token
     */
    @NotNull
    Mono<Oauth2AccessTokenResponse> handleResponse(@NotNull WebClient.ResponseSpec responseSpec);
}
