package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

/**
 * Represent the {@link Oauth2AccessToken} that can be used by Oauth2 Client to access protected resources on resource-server.
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2AccessToken {
    /**
     * Unique token value that represent the access token.
     *
     * access_token (required) The access token string as issued by the authorization server.
     */
    @NotNull
    String tokenValue;
    /**
     * Time at which this {@link Oauth2AccessToken} has been issued
     */
    @NotNull
    Instant issuedAt;
    /**
     * Time at which {@link Oauth2AccessToken} will expire
     */
    @NotNull
    Instant expiresIn;
    /**
     * Scopes associated with this {@link Oauth2AccessToken}. Can differ from requested scopes
     */
    @NotNull
    @Builder.Default
    ScopeContainer scopes = ScopeContainer.empty();

    /**
     * @return true if the token is expired, false otherwise
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresIn);
    }

    /**
     * Retrieves the total lifetime of this token.
     * The `lifetime()` method returns the entire duration of the token from its issuance to expiration.
     * Unlike the {@link #remainingLifetimeSeconds()} method, which provides the remaining time,
     * this method gives the complete lifespan of the token.
     *
     * @return A {@link Duration} representing the entire lifetime of the token.
     */
    public Duration lifetime() {
        return Duration.ofSeconds(expiresIn.getEpochSecond() - issuedAt.getEpochSecond());
    }

    /**
     * Calculate and return remaining lifetime of this token, in seconds
     * @return - remaining {@link Duration} (lifetime) for this token to live
     */
    public Duration remainingLifetimeSeconds() {
        return Duration.ofSeconds(expiresIn.getEpochSecond() - Instant.now().getEpochSecond());
    }
}
