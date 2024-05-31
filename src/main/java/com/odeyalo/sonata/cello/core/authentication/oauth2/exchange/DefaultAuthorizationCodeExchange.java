package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

/**
 * Default implementation of {@link AuthorizationCodeExchange} that uses {@link Oauth2ProviderRegistration} to get the info about client
 * {@link Oauth2AccessTokenResponseHandler} is used to handle the response from remote service
 */
public final class DefaultAuthorizationCodeExchange implements AuthorizationCodeExchange {
    private final Oauth2ProviderRegistration providerRegistration;
    private final WebClient webClient;
    private final Oauth2AccessTokenResponseHandler responseHandler;

    private static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";

    public DefaultAuthorizationCodeExchange(final Oauth2ProviderRegistration providerRegistration,
                                            final WebClient webClient,
                                            final Oauth2AccessTokenResponseHandler responseHandler) {
        this.providerRegistration = providerRegistration;
        this.webClient = webClient;
        this.responseHandler = responseHandler;
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessTokenResponse> exchange(@NotNull final AuthorizationCode code) {
        final URI tokenExchangeUri = CodeExchangeUriBuilder.exchangeUsing(providerRegistration)
                .withAuthorizationCode(code)
                .asUri();

        final WebClient.ResponseSpec responseSpec = webClient.post()
                .uri(tokenExchangeUri)
                .retrieve();

        return responseHandler.handleResponse(responseSpec);
    }

    @Override
    public boolean supports(final @NotNull String providerName) {
        return Objects.equals(providerRegistration.getName(), providerName);
    }

    static final class CodeExchangeUriBuilder {
        private final String tokenEndpoint;
        private String clientId;
        private String clientSecret;
        private String callbackUri;
        private String authorizationCode;

        private CodeExchangeUriBuilder(final String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
        }

        public static CodeExchangeUriBuilder exchangeUsing(final String tokenEndpoint) {
            return new CodeExchangeUriBuilder(tokenEndpoint);
        }

        public static CodeExchangeUriBuilder exchangeUsing(final Oauth2ProviderRegistration providerInfo) {
            return exchangeUsing(providerInfo.getTokenEndpoint())
                    .withClientId(providerInfo.getClientId())
                    .withClientSecret(providerInfo.getClientSecret())
                    .withCallbackUri(providerInfo.getRedirectUri());
        }

        public CodeExchangeUriBuilder withClientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        public CodeExchangeUriBuilder withClientSecret(final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public CodeExchangeUriBuilder withCallbackUri(final String callbackUri) {
            this.callbackUri = callbackUri;
            return this;
        }

        public CodeExchangeUriBuilder withAuthorizationCode(final String authorizationCode) {
            this.authorizationCode = authorizationCode;
            return this;
        }

        public CodeExchangeUriBuilder withAuthorizationCode(final AuthorizationCode authorizationCode) {
            return withAuthorizationCode(authorizationCode.value());
        }

        @NotNull
        public URI asUri() {
            return UriComponentsBuilder.fromUriString(tokenEndpoint)
                    .queryParam(GRANT_TYPE, AUTHORIZATION_CODE_GRANT_TYPE)
                    .queryParam(CLIENT_ID, clientId)
                    .queryParam(CLIENT_SECRET, clientSecret)
                    .queryParam(REDIRECT_URI, callbackUri)
                    .queryParam(AUTHORIZATION_CODE, authorizationCode)
                    .build()
                    .toUri();
        }
    }
}
