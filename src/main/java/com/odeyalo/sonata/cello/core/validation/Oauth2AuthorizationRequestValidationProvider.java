package com.odeyalo.sonata.cello.core.validation;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Interface that should(but not mandatory to) handle only one type of {@link Oauth2AuthorizationRequest}.
 * It is created in this way because Oauth2 flows can have different error codes, error handling, etc.
 * See methods docs for more info.
 * Used as delegate in {@link ProviderOauth2AuthorizationRequestValidator}
 *
 * @see ProviderOauth2AuthorizationRequestValidator
 * @see ImplicitOauth2AuthorizationRequestValidationProvider
 */
public interface Oauth2AuthorizationRequestValidationProvider {
    /**
     * Method to indicate if this implementation is capable to validate the given {@link Oauth2AuthorizationRequest}
     *
     * @param request - request that can be validated in {@link #validate(Oauth2AuthorizationRequest)}
     * @return - {@link Mono} with {@link Boolean#TRUE} if this implementation is capable to validate this {@link Oauth2AuthorizationRequest},
     * {@link Mono} with {@link Boolean#FALSE} if implementation does not support this type of {@link Oauth2AuthorizationRequest}
     */
    @NotNull
    Mono<Boolean> supports(@NotNull Oauth2AuthorizationRequest request);

    /**
     * Validate the given {@link Oauth2AuthorizationRequest}
     *
     * @param request - request to validate
     * @return - {@link Mono} with {@link Void} on success,
     * {@link Mono} with error if failed.
     * @throws Oauth2AuthorizationRequestValidationException - if validation failed
     */
    @NotNull
    Mono<Void> validate(@NotNull Oauth2AuthorizationRequest request);

}