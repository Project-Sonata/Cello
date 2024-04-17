package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

@Component
public final class DefaultOauth2ProviderRedirectUriGenerator implements Oauth2ProviderRedirectUriGenerator {
    private final Oauth2ProviderRegistrationRepository registrationRepository;

    public DefaultOauth2ProviderRedirectUriGenerator(Oauth2ProviderRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @NotNull
    public Mono<URI> generateOauth2RedirectUri(@NotNull String providerName,
                                               @NotNull Oauth2AuthenticationRedirectUriGenerationContext context) {

        return registrationRepository.findByProviderName(providerName)
                .map(provider -> createRedirectUri(provider, context))
                .switchIfEmpty(
                        notSupportedOauth2ProviderException(providerName)
                );
    }

    @NotNull
    private static URI createRedirectUri(Oauth2ProviderRegistration oauth2ProviderRegistration,
                                         Oauth2AuthenticationRedirectUriGenerationContext context) {
        return UriComponentsBuilder.fromUriString(oauth2ProviderRegistration.getProviderUri())
                .queryParam(STATE, context.getState())
                .queryParam(RESPONSE_TYPE, "code")
                .queryParam(REDIRECT_URI, oauth2ProviderRegistration.getRedirectUri())
                .queryParam(CLIENT_ID, oauth2ProviderRegistration.getClientId())
                .queryParam(SCOPE, oauth2ProviderRegistration.getScopes().asOauth2String())
                .build().toUri();
    }

    @NotNull
    private static <T> Mono<T> notSupportedOauth2ProviderException(@NotNull String providerName) {
        return Mono.defer(
                () -> Mono.error(
                        NotSupportedOauth2ProviderException.withCustomMessage("Oauth2 provider: %s not supported", providerName)
                )
        );
    }
}
