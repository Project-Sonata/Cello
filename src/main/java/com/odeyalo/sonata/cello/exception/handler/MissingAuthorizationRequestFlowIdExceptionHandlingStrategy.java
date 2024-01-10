package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.exception.MissingAuthorizationRequestFlowIdException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Handle the {@link MissingAuthorizationRequestFlowIdException}, by default return the
 * 400 BAD REQUEST Http status code and message as response body
 */
@Component
public class MissingAuthorizationRequestFlowIdExceptionHandlingStrategy implements ExceptionHandlingStrategy {

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull Throwable err) {
        return Mono.just(
                err instanceof MissingAuthorizationRequestFlowIdException
        );
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        MissingAuthorizationRequestFlowIdException missingAuthorizationRequestFlowIdException = (MissingAuthorizationRequestFlowIdException) ex;

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);

        return response.writeWith(Flux.just(
                response.bufferFactory().wrap(missingAuthorizationRequestFlowIdException.getMessage().getBytes())
        ));
    }
}
