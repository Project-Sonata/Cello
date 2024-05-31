package com.odeyalo.sonata.cello.core;

/**
 * Contains parameters that can be present in response from the authorization provider during Oauth2 exchange schema
 */
public class Oauth2TokenExchangeResponseParameters {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String TOKEN_TYPE = "token_type";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String ID_TOKEN = "id_token";
    public static final String SCOPE = "scope";
}
