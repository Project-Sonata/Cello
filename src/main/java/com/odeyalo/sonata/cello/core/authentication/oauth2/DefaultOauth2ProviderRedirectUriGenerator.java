package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.Oauth2RequestParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

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
                .map(provider -> createRedirectUri(provider, context));
    }

    @NotNull
    private static URI createRedirectUri(Oauth2ProviderRegistration oauth2ProviderRegistration,
                                         Oauth2AuthenticationRedirectUriGenerationContext context) {
        return UriComponentsBuilder.fromUriString(oauth2ProviderRegistration.getProviderUri())
                .queryParam(Oauth2RequestParameters.STATE, context.getState())
                .queryParam(Oauth2RequestParameters.RESPONSE_TYPE, "code")
                .queryParam(Oauth2RequestParameters.REDIRECT_URI, oauth2ProviderRegistration.getRedirectUri())
                .queryParam(Oauth2RequestParameters.CLIENT_ID, oauth2ProviderRegistration.getClientId())
                .queryParam(Oauth2RequestParameters.SCOPE, oauth2ProviderRegistration.getScopes().asOauth2String())
                .build().toUri();
    }
}
