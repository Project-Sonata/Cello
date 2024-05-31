package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

/**
 * Thrown when a {@link AuthorizationCode} can't be exchanged for access token
 */
public final class AuthorizationCodeExchangeException extends RuntimeException {
    public AuthorizationCodeExchangeException() {
        super();
    }

    public AuthorizationCodeExchangeException(final String message) {
        super(message);
    }

    public AuthorizationCodeExchangeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
