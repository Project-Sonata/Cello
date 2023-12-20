package com.odeyalo.sonata.cello.spring.auth;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Extension of AbstractAuthenticationToken, that wraps the cookie-based resource owner authentication
 */
public final class CelloOauth2CookieResourceOwnerAuthentication extends AbstractAuthenticationToken {
    @NotNull
    private final String cookieValue;
    
    private static final Object NULL_CREDENTIALS = null;
    
    private CelloOauth2CookieResourceOwnerAuthentication(@NotNull String cookieValue) {
        super(null);
        this.cookieValue = cookieValue;
    }
    
    public static CelloOauth2CookieResourceOwnerAuthentication unauthenticated(@NotNull String cookieValue) {
        return new CelloOauth2CookieResourceOwnerAuthentication(cookieValue);
    }

    @Override
    @Nullable
    public Object getCredentials() {
        return NULL_CREDENTIALS;
    }

    @Override
    @NotNull
    public Object getPrincipal() {
        return cookieValue;
    }
}
