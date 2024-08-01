package com.odeyalo.sonata.cello.core.grant;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Stategy to convert {@link AccessTokenResponse} in Http outbound message that will be sent to the client
 */
public interface AccessTokenResponseConverter {
    /**
     * @param response - response to check
     * @return {@link Mono} with {@code true} if this converter can convert this type of response,
     * {@link Mono} with {@code false} otherwise
     */
    @NotNull
    Mono<Boolean> supports(AccessTokenResponse response);

    /**
     * Write the given access token response in {@link ServerWebExchange}
     * @param response - response to write
     * @param writeTo - destination to write the response
     * @return {@link Mono} with {@link Void} on success
     */
    @NotNull
    Mono<Void> writeResponse(@NotNull AccessTokenResponse response,
                             @NotNull ServerWebExchange writeTo);

}
