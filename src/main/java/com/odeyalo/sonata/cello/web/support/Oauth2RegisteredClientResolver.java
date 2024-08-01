package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClientAuthenticationAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public final class Oauth2RegisteredClientResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NotNull final MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Oauth2RegisteredClient.class);
    }

    @Override
    @NotNull
    public Mono<Object> resolveArgument(@NotNull final MethodParameter parameter,
                                        @NotNull final BindingContext bindingContext,
                                        @NotNull final ServerWebExchange exchange) {

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("A client is not authenticated!")))
                .map(SecurityContext::getAuthentication)
                .cast(Oauth2RegisteredClientAuthenticationAdapter.class)
                .map(Oauth2RegisteredClientAuthenticationAdapter::getOauth2RegisteredClient);
    }
}
