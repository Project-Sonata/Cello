package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.Scope;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * Represent the authenticated resource owner with basic info such principal and, optionally, credentials
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public abstract class AuthenticatedResourceOwnerAuthentication implements Authentication {
    @NotNull
    String principal;
    @Nullable
    Object credentials;
    @NotNull
    ResourceOwner resourceOwner;

    @Override
    public String getName() {
        return principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resourceOwner.getAvailableScopes()
                .stream()
                .map(Scope::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public ResourceOwner getDetails() {
        return resourceOwner;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("The resource owner is already authenticated. It cannot be changed");
    }
}