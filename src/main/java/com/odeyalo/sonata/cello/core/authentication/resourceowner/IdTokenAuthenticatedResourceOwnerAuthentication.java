package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a {@link AuthenticatedResourceOwnerAuthentication} that was performed using OAuth 2.0 provider such as Google, GitHub, Facebook, etc using ID token.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Value
public class IdTokenAuthenticatedResourceOwnerAuthentication extends AuthenticatedResourceOwnerAuthentication {
    @NotNull
    String idToken;

    public IdTokenAuthenticatedResourceOwnerAuthentication(@NotNull final String principal,
                                                           @NotNull final ResourceOwner resourceOwner,
                                                           @NotNull final String idToken) {
        super(principal, null, resourceOwner);
        this.idToken = idToken;
    }

    public static IdTokenAuthenticatedResourceOwnerAuthentication create(@NotNull final String principal,
                                                                         @NotNull final ResourceOwner resourceOwner,
                                                                         @NotNull final String idToken) {
        return new IdTokenAuthenticatedResourceOwnerAuthentication(principal, resourceOwner, idToken);
    }
}
