package com.odeyalo.sonata.cello.exception;

/**
 * Should be thrown when {@link com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken} cannot be generated
 */
public class Oauth2AccessTokenGenerationException extends Oauth2TokenGenerationException {

    public static Oauth2AccessTokenGenerationException defaultException() {
        return new Oauth2AccessTokenGenerationException();
    }

    public static Oauth2AccessTokenGenerationException withCustomMessage(String message) {
        return new Oauth2AccessTokenGenerationException(message);
    }

    public static Oauth2AccessTokenGenerationException withMessageAndCause(String message, Throwable cause) {
        return new Oauth2AccessTokenGenerationException(message, cause);
    }

    public Oauth2AccessTokenGenerationException() {
        super();
    }

    public Oauth2AccessTokenGenerationException(String message) {
        super(message);
    }

    public Oauth2AccessTokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oauth2AccessTokenGenerationException(Throwable cause) {
        super(cause);
    }
}
