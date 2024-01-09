package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.exception.handler.ExceptionHandlingStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Handle exceptions occurred in Cello
 */
@RestControllerAdvice
@Order(value = HIGHEST_PRECEDENCE)
public class ExceptionHandlerController implements ErrorWebExceptionHandler {
    private final List<ExceptionHandlingStrategy> exceptionHandlers;

    public ExceptionHandlerController(List<ExceptionHandlingStrategy> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        return Flux.fromIterable(exceptionHandlers)
                .filterWhen(strategy -> strategy.supports(ex))
                .flatMap(strategy -> strategy.handle(exchange, ex))
                .next();
    }
}
