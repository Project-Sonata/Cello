package com.odeyalo.sonata.cello.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Wrapper for Authorization request
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class AuthorizationRequest {
    String responseType;
    String clientId;
    String redirectUri;
    String scope;
    String state;
}
