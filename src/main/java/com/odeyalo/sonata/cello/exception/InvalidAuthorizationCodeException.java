package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchangeException;

public final class InvalidAuthorizationCodeException extends AuthorizationCodeExchangeException {
    static final String MESSAGE = "Provided authorization code does not exist";

    public static InvalidAuthorizationCodeException defaultException() {
        return new InvalidAuthorizationCodeException(MESSAGE);
    }

    public static InvalidAuthorizationCodeException withCustomMessage(final String message) {
        return new InvalidAuthorizationCodeException(message);
    }

    public static InvalidAuthorizationCodeException withMessageAndCause(final String message, final Throwable cause) {
        return new InvalidAuthorizationCodeException(message, cause);
    }

    public InvalidAuthorizationCodeException() {
        super();
    }

    public InvalidAuthorizationCodeException(final String message) {
        super(message);
    }

    public InvalidAuthorizationCodeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
