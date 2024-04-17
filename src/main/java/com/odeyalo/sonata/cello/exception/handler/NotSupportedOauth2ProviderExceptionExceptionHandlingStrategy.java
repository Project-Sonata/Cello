package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public final class NotSupportedOauth2ProviderExceptionExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Throwable err) {
        return Mono.just(
                err instanceof NotSupportedOauth2ProviderException
        );
    }

    @Override
    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

        return exchange.getResponse().writeWith(
                Mono.just(DefaultDataBufferFactory.sharedInstance.wrap(
                        ex.getMessage().getBytes()
                ))
        ).then(exchange.getResponse().setComplete());
    }
}
