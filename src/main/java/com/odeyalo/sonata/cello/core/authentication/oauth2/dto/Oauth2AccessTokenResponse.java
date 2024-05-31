package com.odeyalo.sonata.cello.core.authentication.oauth2.dto;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Represents a successful response from authorization server to a token exchange.
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-5">Section 5 of RFC6749</a>.
 */
public interface Oauth2AccessTokenResponse {

    /**
     * REQUIRED.  The access token issued by the authorization server.
     *
     * @return access token returned by OAuth 2.0 server
     */
    @NotNull
    String getAccessTokenValue();

    /**
     * RECOMMENDED.  The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     *
     * @return - seconds after which token will expire. If response does not contain expires_in param, then default should be used
     */
    int getExpiresIn();

    /**
     * REQUIRED.  The type of the token issued as described in
     * Section 7.1.  Value is case insensitive.
     * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-7.1">Section 7.1</a>
     *
     * @return - type of the token
     */
    @NotNull
    String getTokenType();

    /**
     * OPTIONAL.  The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-6">Refreshing access token</a>.
     *
     * @return - refresh token that can be used to get a new access token or null
     */
    @Nullable
    String getRefreshTokenValue();

    /**
     * OPTIONAL, if identical to the scope requested by the client;
     * otherwise, REQUIRED.  The scope of the access token as
     * described by <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">Section 3.3</a>.
     *
     * @return - scopes returned in response wrapped in {@link ScopeContainer} or empty {@link ScopeContainer} if 'scope' parameter was not present in response
     */
    @NotNull
    ScopeContainer getScopes();

    /**
     * Factory to create a new {@link Oauth2AccessTokenResponse}. Note, that factory can produce any type of {@link Oauth2AccessTokenResponse}.
     * <p>
     * If received response contains 'id_token' parameter, then {@link Factory} MUST return implementation of {@link OpenIdOauth2AccessTokenResponse}
     */
    interface Factory {

        @NotNull
        Oauth2AccessTokenResponse create(@NotNull Map<String, Object> body);

    }
}
