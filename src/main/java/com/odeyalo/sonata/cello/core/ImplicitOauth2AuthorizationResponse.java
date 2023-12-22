package com.odeyalo.sonata.cello.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2.2">Implicit Access Token Response</a>
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class ImplicitOauth2AuthorizationResponse implements Oauth2AuthorizationResponse {
    /**
     * REQUIRED.  The access token issued by the authorization server.
     */
    @NotNull
    String accessToken;
    /**
     * REQUIRED.  The type of the token issued as described in
     * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-7.1">Section 7.1.</a>  Value is case insensitive.
     */
    @NotNull
    String tokenType;
    /**
     * RECOMMENDED.  The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     */
    @Nullable
    Long expiresIn;
    /**
     * OPTIONAL, if identical to the scope requested by the client;
     * otherwise, REQUIRED.  The scope of the access token as
     * described by <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">Section 3.3.</a>
     */
    @Nullable
    ScopeContainer scope;
    /**
     * REQUIRED if the "state" parameter was present in the client
     * authorization request.  The exact value received from the
     * client.
     */
    @Nullable
    String state;

    public static final String BEARER_TOKEN_TYPE = "Bearer";
}
