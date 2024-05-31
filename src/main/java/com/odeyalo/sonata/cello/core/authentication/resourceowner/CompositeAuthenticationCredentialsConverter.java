package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public final class CompositeAuthenticationCredentialsConverter implements AuthenticationCredentialsConverter {
    private final List<AuthenticationCredentialsConverter> delegates;

    public CompositeAuthenticationCredentialsConverter(final List<AuthenticationCredentialsConverter> delegates) {
        this.delegates = delegates;
    }

    @Override
    public @NotNull Mono<AuthenticationCredentials> convertToCredentials(@NotNull final ServerWebExchange exchange) {
        return Flux.fromIterable(delegates)
                .flatMap(it -> it.convertToCredentials(exchange))
                .next();
    }
}
