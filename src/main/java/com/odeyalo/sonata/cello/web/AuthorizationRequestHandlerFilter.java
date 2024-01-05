package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2ErrorCode.INVALID_REQUEST;

/**
 * Handle the creation, validation and saving of {@link com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest}
 */
public class AuthorizationRequestHandlerFilter implements WebFilter {
    private final Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter;
    private final Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator;
    private final Oauth2AuthorizationRequestRepository authorizationRequestRepository;

    public AuthorizationRequestHandlerFilter(Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter,
                                             Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator,
                                             Oauth2AuthorizationRequestRepository authorizationRequestRepository) {
        this.oauth2AuthorizationRequestConverter = oauth2AuthorizationRequestConverter;
        this.oauth2AuthorizationRequestValidator = oauth2AuthorizationRequestValidator;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull ServerWebExchange exchange,
                             @NotNull WebFilterChain chain) {

        return processAuthorizationRequest(exchange)
                .then(chain.filter(exchange));
    }

    @NotNull
    private Mono<Void> processAuthorizationRequest(@NotNull ServerWebExchange exchange) {
        return oauth2AuthorizationRequestConverter.convert(exchange)
                .flatMap(request -> oauth2AuthorizationRequestValidator.validate(request)
                        .thenReturn(request))
                .flatMap(authorizationRequest -> authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, exchange)
                        .thenReturn(authorizationRequest))
                .switchIfEmpty(authorizationRequestRepository.loadAuthorizationRequest(exchange))
                .switchIfEmpty(Mono.error(
                        () -> Oauth2AuthorizationRequestValidationException.errorCodeOnly(INVALID_REQUEST)
                ))
                .then();
    }
}
