package com.odeyalo.sonata.cello.exception;

/**
 * Thrown when a request contains invalid payload(invalid parameters, missing parameters, invalid body, etc.)
 */
public final class MalformedAccessTokenRequestException extends RuntimeException {

    public MalformedAccessTokenRequestException(final String message) {
        super(message);
    }

    public MalformedAccessTokenRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
