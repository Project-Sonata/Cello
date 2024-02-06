package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Component
public class Oauth2ProviderRedirectUriProvider {
    Map<String, Oauth2ProviderRegistration> providers;

    public Oauth2ProviderRedirectUriProvider(Map<String, Oauth2ProviderRegistration> providers) {
        this.providers = providers;
    }

    public Mono<URI> getProviderRedirectUri(String providerName) {
        if ( !providers.containsKey(providerName) ) {
            return Mono.empty();
        }

        return Mono.fromCallable(() -> {
            Oauth2ProviderRegistration oauth2ProviderRegistration = providers.get(providerName);

            return UriComponentsBuilder.fromUriString(oauth2ProviderRegistration.getProviderUri())
                    .queryParam("state", UUID.randomUUID().toString())
                    .queryParam("response_type", "code")
                    .queryParam("redirect_uri", oauth2ProviderRegistration.getRedirectUri())
                    .queryParam("client_id", oauth2ProviderRegistration.getClientId())
                    .queryParam("scope", String.join(" ", oauth2ProviderRegistration.getScopes()))
                    .build().toUri();
        });
    }
}
