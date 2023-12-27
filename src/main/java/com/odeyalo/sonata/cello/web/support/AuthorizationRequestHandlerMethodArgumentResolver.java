package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.Oauth2RequestParameters;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolve the {@link Oauth2AuthorizationRequest} from {@link ServerWebExchange}
 */
public class AuthorizationRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter;
    private final Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator;

    public AuthorizationRequestHandlerMethodArgumentResolver(Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter,
                                                             Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator) {
        this.oauth2AuthorizationRequestConverter = oauth2AuthorizationRequestConverter;
        this.oauth2AuthorizationRequestValidator = oauth2AuthorizationRequestValidator;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Oauth2AuthorizationRequest.class);
    }

    @Override
    @NotNull
    public Mono<Object> resolveArgument(@NotNull MethodParameter parameter,
                                        @NotNull BindingContext bindingContext,
                                        @NotNull ServerWebExchange exchange) {

        return oauth2AuthorizationRequestConverter.convert(exchange)
                .onErrorResume(ex -> missingRequestParameterException(ex.getMessage(), parameter))
                // If we cannot process the request, then, probably, response_type is missing or not supported, return it as response
                .switchIfEmpty(missingRequestParameterException(Oauth2RequestParameters.RESPONSE_TYPE, parameter))
                .flatMap(request -> oauth2AuthorizationRequestValidator.validate(request)
                                .thenReturn(request)
                );
    }

    @NotNull
    private static <T> Mono<T> missingRequestParameterException(@NotNull String name, @NotNull MethodParameter parameter) {
        return Mono.error(new MissingRequestValueException(name, Oauth2AuthorizationRequest.class, "Request parameter", parameter));
    }
}