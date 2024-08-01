package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.client.authentication.AuthenticationStrategy;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class InvalidClientCredentialsException extends RuntimeException {
    AuthenticationStrategy usedAuthenticationStrategy;

    public InvalidClientCredentialsException(final String message, final AuthenticationStrategy usedAuthenticationStrategy) {
        super(message);
        this.usedAuthenticationStrategy = usedAuthenticationStrategy;
    }

    public InvalidClientCredentialsException(final String message, final AuthenticationStrategy usedAuthenticationStrategy, final Throwable cause) {
        super(message, cause);
        this.usedAuthenticationStrategy = usedAuthenticationStrategy;
    }
}
