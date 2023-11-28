package com.odeyalo.sonata.cello.core.authentication;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

/**
 * {@link ResourceOwnerPreAuthentication} impl that uses the username and password as login strategy
 */
@EqualsAndHashCode(callSuper = true)
@Value
@SuperBuilder
public class UsernamePasswordResourceOwnerPreAuthentication extends ResourceOwnerPreAuthentication {

    private UsernamePasswordResourceOwnerPreAuthentication(String username, String password) {
        super(username, password);
    }

    public static UsernamePasswordResourceOwnerPreAuthentication withCredentials(String username, String password) {
        return new UsernamePasswordResourceOwnerPreAuthentication(username, password);
    }
}
