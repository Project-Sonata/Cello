package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the authenticated resource owner with basic info such principal and, optionally, credentials
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public abstract class AuthenticatedResourceOwnerAuthentication {
    @NotNull
    String principal;
    @Nullable
    Object credentials;
    @NotNull
    ResourceOwner resourceOwner;
}