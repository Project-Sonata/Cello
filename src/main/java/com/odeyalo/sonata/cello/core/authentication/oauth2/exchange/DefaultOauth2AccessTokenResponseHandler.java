package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class DefaultOauth2AccessTokenResponseHandler implements Oauth2AccessTokenResponseHandler {
    private final Oauth2AccessTokenResponse.Factory responseFactory;
    private final Logger logger = LoggerFactory.getLogger(DefaultOauth2AccessTokenResponseHandler.class);

    public DefaultOauth2AccessTokenResponseHandler(final Oauth2AccessTokenResponse.Factory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessTokenResponse> handleResponse(@NotNull final WebClient.ResponseSpec responseSpec) {
        return responseSpec
                .onStatus(HttpStatusCode::isError, resp -> authorizationCodeExchangeException(resp))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(responseFactory::create)
                .doOnError(err -> logger.error("Authorization code exchange failed", err));
    }

    @NotNull
    private static Mono<Throwable> authorizationCodeExchangeException(final ClientResponse resp) {
        return Mono.error(
                new AuthorizationCodeExchangeException(
                        String.format("Failed to exchange authorization code. " +
                                        "Expected: 200 OK status, but was: %s. \nHeaders: %s",
                                resp.statusCode(), resp.headers().asHttpHeaders())));
    }
}
