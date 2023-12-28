package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.core.Oauth2RequestParameters;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
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
                .switchIfEmpty(
                        Mono.error(
                                Oauth2AuthorizationRequestValidationException.errorCodeOnly(Oauth2ErrorCode.INVALID_REQUEST)
                        ))
                .flatMap(request -> oauth2AuthorizationRequestValidator.validate(request)
                        .thenReturn(request)
                );
    }
}