package com.odeyalo.sonata.cello.core.authentication.oauth2.dto;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * An extension to {@link Oauth2AccessTokenResponse} that add support for OIDC.
 */
public interface OpenIdOauth2AccessTokenResponse extends Oauth2AccessTokenResponse {
    /**
     * ID token that was returned in response
     * @return - ID token value
     */
    @NotNull
    String getIdToken();

    interface OpenIdFactory extends Factory {

        @Override
        @NotNull
        OpenIdOauth2AccessTokenResponse create(@NotNull Map<String, Object> body);
    }
}
