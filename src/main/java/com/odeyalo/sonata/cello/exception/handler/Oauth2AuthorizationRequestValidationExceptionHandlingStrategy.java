package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class Oauth2AuthorizationRequestValidationExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Throwable err) {
        return Mono.just(
                err instanceof Oauth2AuthorizationRequestValidationException
        );
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        return Mono.fromRunnable(
                () -> response.setStatusCode(HttpStatus.BAD_REQUEST)
        );
    }
}
