package com.odeyalo.sonata.cello.core.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Default implementation of Oauth2ClientInfo, that does not provide any additional info about client
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmptyOauth2ClientInfo implements Oauth2ClientInfo {
    private static final EmptyOauth2ClientInfo INSTANCE = new EmptyOauth2ClientInfo();

    public static EmptyOauth2ClientInfo create() {
        return INSTANCE;
    }
}
