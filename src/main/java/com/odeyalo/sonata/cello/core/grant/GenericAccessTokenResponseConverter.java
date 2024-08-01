package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.web.support.ResponseWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * Generic {@link AccessTokenResponseConverter} that converts any {@link AccessTokenResponse}
 */
public final class GenericAccessTokenResponseConverter implements AccessTokenResponseConverter {
    private final ResponseWriter accessTokenResponseWriter;

    public GenericAccessTokenResponseConverter(final ResponseWriter accessTokenResponseWriter) {
        this.accessTokenResponseWriter = accessTokenResponseWriter;
    }

    @Override
    @NotNull
    public Mono<Boolean> supports(final AccessTokenResponse response) {
        return Mono.just(true);
    }

    @Override
    @NotNull
    public Mono<Void> writeResponse(@NotNull final AccessTokenResponse response,
                                    @NotNull final ServerWebExchange writeTo) {

        final Map<String, Object> responseBody = Map.of(
                "access_token", response.getAccessTokenValue(),
                "expires_in", Objects.requireNonNullElse(response.getExpiresIn(), "null"),
                "token_type", response.getTokenType()
        );

        return accessTokenResponseWriter.writeResponse(responseBody, writeTo);
    }
}
