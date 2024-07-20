package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.ClaimsWrapperFactory;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A holder for authorization code claims
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationCodeClaims {
    @NotNull
    @Getter(value = AccessLevel.PRIVATE)
    Map<String, Object> claims = new HashMap<>();

    public static AuthorizationCodeClaims empty() {
        return new AuthorizationCodeClaims();
    }

    /**
     * Wrap this claims in the specified type
     *
     * @param wrapperFactory - a factory to create an instance of {@link T} with
     * @param <T>            - a type to wrap the claims
     * @return - wrapped claims
     * @throws IllegalStateException if the required key is missing or value is invalid
     * @see ClaimsWrapperFactory
     */
    @NotNull
    public <T> T wrap(@NotNull final ClaimsWrapperFactory<T> wrapperFactory) {
        return wrapperFactory.create(claims);
    }

    public int size() {
        return claims.size();
    }

    public boolean isEmpty() {
        return claims.isEmpty();
    }

    public boolean containsKey(final String key) {
        return claims.containsKey(key);
    }

    @Nullable
    public <T> T get(@NotNull final String key, @NotNull final Class<T> requiredType) {
        final Object claimValue = claims.get(key);
        if ( claimValue == null ) {
            return null;
        }
        return requiredType.cast(claimValue);
    }

    public Object put(@NotNull final String key, @NotNull final Object value) {
        return claims.put(key, value);
    }

    public String getState() {
        return (String) claims.get("state");
    }

    public void putState(@NotNull final String state) {
        claims.put("state", state);
    }
}
