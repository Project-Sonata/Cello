package com.odeyalo.sonata.cello.core.validation;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Interface that used to validate the {@link Oauth2AuthorizationRequest}
 */
public interface Oauth2AuthorizationRequestValidator {
    /**
     * Validate the given request in any possible way.
     *
     * @param request - request to validate
     * @return - {@link Mono} with {@link Void} if request is validated successfully,
     * {@link Mono} with error if the request failed validation or provided type of {@link Oauth2AuthorizationRequest} does not supported
     * @throws Oauth2AuthorizationRequestValidationException if request validation failed
     *                                                       or {@link Oauth2AuthorizationRequest} can't be validated(not supported implementation, no validator, etc)
     */
    @NotNull
    Mono<Void> validate(@NotNull Oauth2AuthorizationRequest request);

}