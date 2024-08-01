package com.odeyalo.sonata.cello.core;

/**
 * Contains default oauth2 response types supported by Cello
 */
public final class DefaultOauth2ResponseTypes {
    public static final Oauth2ResponseType IMPLICIT = Oauth2ResponseType.create("token");
    public static final Oauth2ResponseType AUTHORIZATION_CODE = Oauth2ResponseType.create("code");
}
