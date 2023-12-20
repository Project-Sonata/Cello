package com.odeyalo.sonata.cello.core.authentication.resourceowner.exception;

public class ResourceOwnerAuthenticationException extends RuntimeException {

    public static ResourceOwnerAuthenticationException defaultException() {
        return new ResourceOwnerAuthenticationException();
    }

    public static ResourceOwnerAuthenticationException withCustomMessage(String message) {
        return new ResourceOwnerAuthenticationException(message);
    }

    public static ResourceOwnerAuthenticationException withMessageAndCause(String message, Throwable cause) {
        return new ResourceOwnerAuthenticationException(message, cause);
    }

    public ResourceOwnerAuthenticationException() {
        super();
    }

    public ResourceOwnerAuthenticationException(String message) {
        super(message);
    }

    public ResourceOwnerAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceOwnerAuthenticationException(Throwable cause) {
        super(cause);
    }
}
