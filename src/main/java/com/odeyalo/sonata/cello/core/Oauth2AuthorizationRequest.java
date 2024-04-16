package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent specific Oauth2 authorization request
 *
 * @see ImplicitOauth2AuthorizationRequest
 */
public interface Oauth2AuthorizationRequest {

    /**
     * REQUIRED.  The client identifier as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2.2">Section 2.2.</a>
     */
    @NotNull
    String getClientId();

    /**
     * OPTIONAL.  As described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.1.2">Section 3.1.2.</a>
     */
    @NotNull
    RedirectUri getRedirectUri();

    /**
     * OPTIONAL.  The scope of the access request as described by <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">Section 3.3.</a>
     */
    @NotNull
    ScopeContainer getScopes();

    /**
     * RECOMMENDED.  An opaque value used by the client to maintain
     * state between the request and callback.  The authorization
     * server includes this value when redirecting the user-agent back
     * to the client.
     */
    @Nullable
    String getState();

    /**
     * @return a response type that this authorization request represent
     */
    @NotNull
    Oauth2ResponseType getResponseType();
}
