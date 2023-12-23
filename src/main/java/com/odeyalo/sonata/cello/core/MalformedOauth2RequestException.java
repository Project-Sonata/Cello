package com.odeyalo.sonata.cello.core;

/**
 * Indicates that received request is malformed and cannot be processed by Oauth2 Server
 */
public class MalformedOauth2RequestException extends RuntimeException {

    public static MalformedOauth2RequestException defaultException() {
        return new MalformedOauth2RequestException();
    }

    public static MalformedOauth2RequestException withCustomMessage(String message) {
        return new MalformedOauth2RequestException(message);
    }

    public static MalformedOauth2RequestException withMessageAndCause(String message, Throwable cause) {
        return new MalformedOauth2RequestException(message, cause);
    }

    public MalformedOauth2RequestException() {
        super();
    }

    public MalformedOauth2RequestException(String message) {
        super(message);
    }

    public MalformedOauth2RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedOauth2RequestException(Throwable cause) {
        super(cause);
    }
}
