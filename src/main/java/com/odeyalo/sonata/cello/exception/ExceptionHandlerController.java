package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.exception.handler.ExceptionHandlingStrategy;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Handle exceptions occurred in Cello using a {@link List} of {@link ExceptionHandlingStrategy} instances.
 * If a proper {@link ExceptionHandlingStrategy} was not found for a given error, then exception is being considered as unhandled
 * and {@link Mono#error(Throwable)} with given exception will be returned
 */
@RestControllerAdvice
@Order(value = -2) // To handle an error before DefaultErrorWebExceptionHandler
public final class ExceptionHandlerController implements ErrorWebExceptionHandler {
    private final List<ExceptionHandlingStrategy> exceptionHandlers;
    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    public ExceptionHandlerController(final List<ExceptionHandlingStrategy> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull final ServerWebExchange exchange,
                             @NotNull final Throwable ex) {

        logger.info("An error has been occurred, trying to handle the error: ", ex);

        return Flux.fromIterable(exceptionHandlers)
                .filterWhen(strategy -> strategy.supports(ex))
                .next()
                .doOnNext(handler -> logger.info("Found a proper candidate to handler the error of type: [{}] with: [{}]", ex.getClass().getName(), handler.getClass().getName()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(ex)))
                .flatMap(strategy -> strategy.handle(exchange, ex));
    }
}
