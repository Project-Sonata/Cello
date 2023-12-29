package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Load the {@link Oauth2AuthorizationRequest} from {@link Oauth2AuthorizationRequestRepository}
 */
public class AuthorizationRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Oauth2AuthorizationRequestRepository requestRepository;

    public AuthorizationRequestHandlerMethodArgumentResolver(Oauth2AuthorizationRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
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

        return requestRepository.loadAuthorizationRequest(exchange)
                // required due to java casting
                .map(req -> req);
    }
}