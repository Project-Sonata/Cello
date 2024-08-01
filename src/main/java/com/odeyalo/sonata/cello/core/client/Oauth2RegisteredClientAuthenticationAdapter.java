package com.odeyalo.sonata.cello.core.client;

import lombok.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Value
public class Oauth2RegisteredClientAuthenticationAdapter implements Authentication {
    Oauth2RegisteredClient oauth2RegisteredClient;

    public Oauth2RegisteredClientAuthenticationAdapter(final Oauth2RegisteredClient oauth2RegisteredClient) {
        this.oauth2RegisteredClient = oauth2RegisteredClient;
    }

    public static Oauth2RegisteredClientAuthenticationAdapter wrapOauth2Client(final Oauth2RegisteredClient client) {
        return new Oauth2RegisteredClientAuthenticationAdapter(client);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Oauth2ClientCredentials getCredentials() {
        return oauth2RegisteredClient.getCredentials();
    }

    @Override
    public Oauth2ClientInfo getDetails() {
        return oauth2RegisteredClient.getOauth2ClientInfo();
    }

    @Override
    public String getPrincipal() {
        return oauth2RegisteredClient.getCredentials().getClientId();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Already authenticated");
    }

    @Override
    public String getName() {
        return oauth2RegisteredClient.getCredentials().getClientId();
    }
}
