package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;

/**
 * Exception to throw when {@link Oauth2AuthorizationRequest} cannot be created
 */
public class AuthorizationRequestCreationException extends RuntimeException {
    
    public static AuthorizationRequestCreationException defaultException() {
        return new AuthorizationRequestCreationException();
    }

    public static AuthorizationRequestCreationException withCustomMessage(String message) {
        return new AuthorizationRequestCreationException(message);
    }

    public static AuthorizationRequestCreationException withMessageAndCause(String message, Throwable cause) {
        return new AuthorizationRequestCreationException(message, cause);
    }

    public AuthorizationRequestCreationException() {
        super();
    }

    public AuthorizationRequestCreationException(String message) {
        super(message);
    }

    public AuthorizationRequestCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationRequestCreationException(Throwable cause) {
        super(cause);
    }
}
