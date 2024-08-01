package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.core.grant.ErrorMessage;
import com.odeyalo.sonata.cello.exception.AuthorizationCodeStolenException;
import com.odeyalo.sonata.cello.web.support.ResponseWriter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public final class AuthorizationCodeStolenExceptionHandlingStrategy implements ExceptionHandlingStrategy {
    private final ResponseWriter responseWriter;

    public AuthorizationCodeStolenExceptionHandlingStrategy(final ResponseWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull final Throwable err) {
        return Mono.just(
                err instanceof AuthorizationCodeStolenException
        );
    }

    @Override
    @NotNull
    public Mono<Void> handle(@NotNull final ServerWebExchange exchange, @NotNull final Throwable ex) {
        final ErrorMessage errorMessage = ErrorMessage.builder()
                .error(Oauth2ErrorCode.INVALID_REQUEST.asSpecificationString())
                .description("Authorization code is invalid or can't be used by this client")
                .build();

        exchange.getResponse().setStatusCode(BAD_REQUEST);
        return responseWriter.writeResponse(errorMessage.asMap(), exchange);
    }
}
