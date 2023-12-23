package com.odeyalo.sonata.cello.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the Implicit response type as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2.1">Implicit Authorization Request</a>
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class ImplicitOauth2AuthorizationRequest implements Oauth2AuthorizationRequest {
    /**
     * REQUIRED.  The client identifier as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2.2">Section 2.2.</a>
     */
    @NotNull
    String clientId;
    /**
     * OPTIONAL.  As described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.1.2">Section 3.1.2.</a>
     */
    @Nullable
    RedirectUri redirectUri;
    /**
     * OPTIONAL.  The scope of the access request as described by <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">Section 3.3.</a>
     */
    @NotNull
    @Builder.Default
    ScopeContainer scopes = ScopeContainer.empty();
    /**
     * RECOMMENDED.  An opaque value used by the client to maintain
     * state between the request and callback.  The authorization
     * server includes this value when redirecting the user-agent back
     * to the client.
     */
    @Nullable
    String state;
}