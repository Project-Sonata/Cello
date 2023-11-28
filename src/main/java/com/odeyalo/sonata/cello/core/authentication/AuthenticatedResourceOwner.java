package com.odeyalo.sonata.cello.core.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Represent the resource owner that has been successfully authenticated
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class AuthenticatedResourceOwner {
    String principal;
    Object credentials;
    Object details;
}
