package com.odeyalo.sonata.cello.core;

import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Mock implementation of {@link Oauth2AuthorizationResponse}. Primary used for tests.
 */
@Value
public class MockOauth2AuthorizationResponse implements Oauth2AuthorizationResponse<MockOauth2AuthorizationRequest> {
    @NotNull
    MockOauth2AuthorizationRequest request;

    public static MockOauth2AuthorizationResponse create(MockOauth2AuthorizationRequest request) {
        return new MockOauth2AuthorizationResponse(request);
    }
    @Override
    @NotNull
    public MockOauth2AuthorizationRequest getAssociatedRequest() {
        return request;
    }
}
