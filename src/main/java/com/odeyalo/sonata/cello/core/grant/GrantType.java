package com.odeyalo.sonata.cello.core.grant;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public enum GrantType {
    /**
     * Grant type that used to exchange an authorization code for access token
     * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3">Authorization code Access token request</a>
     */
    AUTHORIZATION_CODE("authorization_code");

    @Nullable
    public static GrantType fromRequestParam(@Nullable final String oauth2QueryParam) {

        if (oauth2QueryParam == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(type -> Objects.equals(type.oauth2Name, oauth2QueryParam))
                .findFirst()
                .orElse(null);
    }

    /**
     * Name associated with this grant type in Oauth2 Spec
     */
    private final String oauth2Name;

    GrantType(final String oauth2Name) {
        this.oauth2Name = oauth2Name;
    }
}
