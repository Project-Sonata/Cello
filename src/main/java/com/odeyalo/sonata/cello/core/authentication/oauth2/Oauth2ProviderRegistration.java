package com.odeyalo.sonata.cello.core.authentication.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2ProviderRegistration {
    @NotNull
    String providerUri;
    @NotNull
    String clientId;
    @NotNull
    String clientSecret;
    @NotNull
    String redirectUri;
    @NotNull
    Set<String> scopes;
}
