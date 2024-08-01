package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.exception.InvalidAuthorizationCodeException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public final class InvalidAuthorizationCodeExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    @Override
    @NotNull
    public  Mono<Boolean> supports(@NotNull final Throwable err) {
        return Mono.just(
                err instanceof InvalidAuthorizationCodeException
        );
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull final ServerWebExchange exchange, @NotNull final Throwable ex) {
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(BAD_REQUEST));
    }
}
