package com.odeyalo.sonata.cello.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for Oauth2 response type, as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.1.1">Response Type Spec</a>
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Oauth2ResponseType {
    @Nullable
    String name;

    public static final Oauth2ResponseType UNKNOWN = new Oauth2ResponseType(null);

    public static Oauth2ResponseType create(@Nullable String name) {
        if ( name == null ) {
            return UNKNOWN;
        }
        return new Oauth2ResponseType(name);
    }
}
