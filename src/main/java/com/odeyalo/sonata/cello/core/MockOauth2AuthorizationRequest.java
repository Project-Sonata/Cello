package com.odeyalo.sonata.cello.core;

/**
 * Mock implementation of {@link Oauth2AuthorizationRequest}. Primary used for tests
 */
public final class MockOauth2AuthorizationRequest implements Oauth2AuthorizationRequest {

    private MockOauth2AuthorizationRequest() {}

    public static MockOauth2AuthorizationRequest create() {
        return new MockOauth2AuthorizationRequest();
    }
}
