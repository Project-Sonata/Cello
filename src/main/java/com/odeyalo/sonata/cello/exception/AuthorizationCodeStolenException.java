package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchangeException;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Thrown when an authorization code is used by different client(different from the one who requested it)
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class AuthorizationCodeStolenException extends AuthorizationCodeExchangeException {
    String stolenAuthorizationCode;


    private static final String MESSAGE = "An authorization code is used by different client";

    public AuthorizationCodeStolenException(final String stolenAuthorizationCode) {
        super(MESSAGE);
        this.stolenAuthorizationCode = stolenAuthorizationCode;
    }
}
