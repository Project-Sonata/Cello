package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.Oauth2RequestParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Component
public final class DefaultOauth2ProviderRedirectUriProviderManager implements Oauth2ProviderRedirectUriProviderManager{
    private final Oauth2ProviderRegistrationRepository registrationRepository;

    public DefaultOauth2ProviderRedirectUriProviderManager(Oauth2ProviderRegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @NotNull
    public Mono<URI> getProviderRedirectUri(@NotNull String providerName) {
        return registrationRepository.findByProviderName(providerName)
                .map(DefaultOauth2ProviderRedirectUriProviderManager::createRedirectUri);
    }

    @NotNull
    private static URI createRedirectUri(Oauth2ProviderRegistration oauth2ProviderRegistration) {
        return UriComponentsBuilder.fromUriString(oauth2ProviderRegistration.getProviderUri())
                .queryParam(Oauth2RequestParameters.STATE, UUID.randomUUID().toString())
                .queryParam(Oauth2RequestParameters.RESPONSE_TYPE, "code")
                .queryParam(Oauth2RequestParameters.REDIRECT_URI, oauth2ProviderRegistration.getRedirectUri())
                .queryParam(Oauth2RequestParameters.CLIENT_ID, oauth2ProviderRegistration.getClientId())
                .queryParam(Oauth2RequestParameters.SCOPE, oauth2ProviderRegistration.getScopes().asOauth2String())
                .build().toUri();
    }
}
