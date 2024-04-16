package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Mock implementation of {@link Oauth2AuthorizationRequest}. Primary used for tests
 */
public final class MockOauth2AuthorizationRequest implements Oauth2AuthorizationRequest {

    private MockOauth2AuthorizationRequest() {}

    public static MockOauth2AuthorizationRequest create() {
        return new MockOauth2AuthorizationRequest();
    }

    @Override
    public @NotNull String getClientId() {
        return "123";
    }

    @Override
    public @NotNull RedirectUri getRedirectUri() {
        return RedirectUri.create("http:localhost:3000/oauth2/callback");
    }

    @Override
    public @NotNull ScopeContainer getScopes() {
        return ScopeContainer.singleScope(
                SimpleScope.withName("read")
        );
    }

    @Override
    public @Nullable String getState() {
        return null;
    }

    @Override
    public @NotNull Oauth2ResponseType getResponseType() {
        return Oauth2ResponseType.UNKNOWN;
    }
}
