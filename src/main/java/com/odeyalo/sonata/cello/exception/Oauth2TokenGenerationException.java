package com.odeyalo.sonata.cello.exception;

/**
 * Should be thrown when any Oauth2 Token cannot be generated
 */
public class Oauth2TokenGenerationException extends RuntimeException {
    public static Oauth2TokenGenerationException defaultException() {
        return new Oauth2TokenGenerationException();
    }

    public static Oauth2TokenGenerationException withCustomMessage(String message) {
        return new Oauth2TokenGenerationException(message);
    }

    public static Oauth2TokenGenerationException withMessageAndCause(String message, Throwable cause) {
        return new Oauth2TokenGenerationException(message, cause);
    }

    public Oauth2TokenGenerationException() {
        super();
    }

    public Oauth2TokenGenerationException(String message) {
        super(message);
    }

    public Oauth2TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oauth2TokenGenerationException(Throwable cause) {
        super(cause);
    }
}
