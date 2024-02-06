package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.Oauth2RequestParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Component
public final class Oauth2ProviderRedirectUriProviderManager {
    private final Map<String, Oauth2ProviderRegistration> providers;

    public Oauth2ProviderRedirectUriProviderManager(Map<String, Oauth2ProviderRegistration> providers) {
        this.providers = providers;
    }

    @NotNull
    public Mono<URI> getProviderRedirectUri(@NotNull String providerName) {
        if ( !providers.containsKey(providerName) ) {
            return Mono.empty();
        }

        return Mono.fromCallable(() -> {
            Oauth2ProviderRegistration oauth2ProviderRegistration = providers.get(providerName);

            return UriComponentsBuilder.fromUriString(oauth2ProviderRegistration.getProviderUri())
                    .queryParam(Oauth2RequestParameters.STATE, UUID.randomUUID().toString())
                    .queryParam(Oauth2RequestParameters.RESPONSE_TYPE, "code")
                    .queryParam(Oauth2RequestParameters.REDIRECT_URI, oauth2ProviderRegistration.getRedirectUri())
                    .queryParam(Oauth2RequestParameters.CLIENT_ID, oauth2ProviderRegistration.getClientId())
                    .queryParam(Oauth2RequestParameters.SCOPE, String.join(" ", oauth2ProviderRegistration.getScopes()))
                    .build().toUri();
        });
    }
}
