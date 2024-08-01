package com.odeyalo.sonata.cello.web.support;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * A strategy to write body in HTTP response
 * @see org.springframework.http.codec.HttpMessageWriter
 */
public interface ResponseWriter {

    @NotNull
    Mono<Void> writeResponse(@NotNull Object body, @NotNull ServerWebExchange writeTo);

}
