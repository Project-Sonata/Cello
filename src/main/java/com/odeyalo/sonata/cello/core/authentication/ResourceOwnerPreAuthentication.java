package com.odeyalo.sonata.cello.core.authentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Base class that contains resource owner UNAUTHENTICATED authentication info.
 * Abstract since it doesn't make much sense to don't know the authentication strategy.
 * Default implementation is {@link UsernamePasswordResourceOwnerPreAuthentication}
 */
@Data
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class ResourceOwnerPreAuthentication implements Serializable {
    String principal;
    Object credentials;
    boolean isAuthenticated = false;
}
