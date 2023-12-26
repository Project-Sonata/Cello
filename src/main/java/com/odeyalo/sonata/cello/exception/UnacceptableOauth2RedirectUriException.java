package com.odeyalo.sonata.cello.exception;

/**
 * Indicates that the redirect uri is not allowed and Oauth2 request should not be handled
 */
public class UnacceptableOauth2RedirectUriException extends RuntimeException {

    public static UnacceptableOauth2RedirectUriException defaultException() {
        return new UnacceptableOauth2RedirectUriException();
    }

    public static UnacceptableOauth2RedirectUriException withCustomMessage(String message) {
        return new UnacceptableOauth2RedirectUriException(message);
    }

    public static UnacceptableOauth2RedirectUriException withMessageAndCause(String message, Throwable cause) {
        return new UnacceptableOauth2RedirectUriException(message, cause);
    }

    public UnacceptableOauth2RedirectUriException() {
        super();
    }

    public UnacceptableOauth2RedirectUriException(String message) {
        super(message);
    }

    public UnacceptableOauth2RedirectUriException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnacceptableOauth2RedirectUriException(Throwable cause) {
        super(cause);
    }
}
