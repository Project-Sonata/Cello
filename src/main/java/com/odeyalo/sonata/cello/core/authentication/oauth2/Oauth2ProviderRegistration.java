package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2ProviderRegistration {
    String name;
    /**
     * A URI to which resource owner will be redirected to authenticate itself
     */
    @NotNull
    String providerUri;
    /**
     * Client ID of the registration that will be appended to {@code providerUri} value, as described in Oauth2 Specification
     */
    @NotNull
    String clientId;
    /**
     * Client secret of the registration that will be used while sending requests to endpoints that require an authentication of Oauth2 client.
     */
    @NotNull
    String clientSecret;
    /**
     * A URI to which resource owner will be redirected after authentication
     * on Oauth2 provider has been completed and resource owner granted or denied access
     */
    @NotNull
    String redirectUri;
    /**
     * Endpoint that will be invoked to exchange an authorization code to access token.
     */
    @Nullable
    String tokenEndpoint;
    /**
     * Scopes to include in Oauth2 request to Oauth2 provider, these scopes will be appended to 'scope' query parameter as described in Oauth2 Specification
     */
    @NotNull
    ScopeContainer scopes;
}
