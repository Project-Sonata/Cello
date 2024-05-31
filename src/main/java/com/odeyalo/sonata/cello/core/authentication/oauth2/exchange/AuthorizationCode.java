package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import org.jetbrains.annotations.NotNull;

/**
 * Represent a Authorization code that can be exchanged for access token
 * @param value - authorization code value
 */
public record AuthorizationCode(@NotNull String value) {

    public static AuthorizationCode wrapString(final String value) {
        return new AuthorizationCode(value);
    }
}
