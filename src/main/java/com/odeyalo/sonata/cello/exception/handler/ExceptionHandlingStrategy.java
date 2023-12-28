package com.odeyalo.sonata.cello.exception.handler;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Strategy to handle the exception and convert it to HTTP response that should be returned to end-user
 */
public interface ExceptionHandlingStrategy {
    /**
     * Invoked before {@link #handle(ServerWebExchange, Throwable)} method to indicate if this implementation is capable to handle this exception
     * @param err - exception to handle
     * @return - {@link Mono} with {@link Boolean#TRUE} if implementation supports this type of exception,
     * {@link Mono} with {@link Boolean#FALSE} otherwise
     */
    @NotNull
    Mono<Boolean> supports(@NotNull Throwable err);

    /**
     * Handle the exception that was thrown, write the response to {@link ServerWebExchange} that will be returned
     * @param exchange - current http exchange
     * @param ex - exception to handle
     * @return - {@link Mono} with {@link Void} on success, {@link Mono} with error on fail
     */
    @NotNull
    Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex);
}
