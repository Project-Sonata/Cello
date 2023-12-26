package com.odeyalo.sonata.cello.core.validation;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Invoke list of {@link Oauth2AuthorizationRequestValidationProvider} and returns first non-empty result
 */
@Component
public class ProviderOauth2AuthorizationRequestValidator implements Oauth2AuthorizationRequestValidator {
    private final List<Oauth2AuthorizationRequestValidationProvider> validationProviders;

    public ProviderOauth2AuthorizationRequestValidator(List<Oauth2AuthorizationRequestValidationProvider> validationProviders) {
        this.validationProviders = validationProviders;
    }

    @Override
    @NotNull
    public Mono<Void> validate(@NotNull Oauth2AuthorizationRequest request) {
        return Flux.fromIterable(validationProviders)
                .filterWhen(provider -> provider.supports(request))
                .next()
                .flatMap(provider -> provider.validate(request));
    }
}
