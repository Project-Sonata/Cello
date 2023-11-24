package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolve the {@link AuthorizationRequest} from {@link ServerWebExchange}
 */
@Component
public class AuthorizationRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthorizationRequestCreator authorizationRequestCreator;

    @Autowired
    public AuthorizationRequestHandlerMethodArgumentResolver(AuthorizationRequestCreator authorizationRequestCreator) {
        this.authorizationRequestCreator = authorizationRequestCreator;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AuthorizationRequest.class);
    }

    @Override
    @NotNull
    public Mono<Object> resolveArgument(@NotNull MethodParameter parameter,
                                        @NotNull BindingContext bindingContext,
                                        @NotNull ServerWebExchange exchange) {

        return authorizationRequestCreator.createAuthorizationRequest(exchange)
                .onErrorResume(ex -> missingRequestParameterException(ex.getMessage(), parameter))
                // Required due to Java casting
                .map(request -> request);
    }

    @NotNull
    private static <T> Mono<T> missingRequestParameterException(@NotNull String name, @NotNull MethodParameter parameter) {
        return Mono.error(new MissingRequestValueException(name, AuthorizationRequest.class, "Request parameter", parameter));
    }
}