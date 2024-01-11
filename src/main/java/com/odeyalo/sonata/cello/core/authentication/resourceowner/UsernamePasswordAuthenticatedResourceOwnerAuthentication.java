package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UsernamePasswordAuthenticatedResourceOwnerAuthentication extends AuthenticatedResourceOwnerAuthentication {

    public UsernamePasswordAuthenticatedResourceOwnerAuthentication(String username, String credentials, ResourceOwner resourceOwner) {
        super(username, credentials, resourceOwner);
    }

    public static UsernamePasswordAuthenticatedResourceOwnerAuthentication create(String username, String credentials, ResourceOwner resourceOwner) {
        return new UsernamePasswordAuthenticatedResourceOwnerAuthentication(username, credentials, resourceOwner);
    }

    @Override
    public String getCredentials() {
        return (String) super.getCredentials();
    }
}
