package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.exception.UnacceptableOauth2RedirectUriException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UnacceptableOauth2RedirectUriExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Throwable err) {
        return Mono.just(
                err instanceof UnacceptableOauth2RedirectUriException
        );
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        return Mono.fromRunnable(
                () -> exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST)
        );
    }
}
