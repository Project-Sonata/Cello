package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

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
     * <p>
     * access_token (required) The access token string as issued by the authorization server.
     */
    @NotNull
    String tokenValue;
    /**
     * Represent the token type of the {@link Oauth2AccessToken}
     */
    @NotNull
    @Builder.Default
    TokenType tokenType = TokenType.BEARER;
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
     * Unlike the {@link #remainingLifetime()} method, which provides the remaining time,
     * this method gives the complete lifespan of the token.
     *
     * @return A {@link Duration} representing the entire lifetime of the token.
     */
    public Duration lifetime() {
        return Duration.ofSeconds(expiresIn.getEpochSecond() - issuedAt.getEpochSecond());
    }

    /**
     * Calculate and return remaining lifetime of this token, in seconds
     *
     * @return - remaining {@link Duration} (lifetime) for this token to live
     */
    public Duration remainingLifetime() {
        return Duration.ofSeconds(expiresIn.getEpochSecond() - Instant.now().getEpochSecond());
    }

    /**
     * Immutable class to represent the Access Token Type.
     * <p>
     * token_type is a parameter in Access Token generate call to Authorization server, which essentially represents how an access_token will be generated and presented for resource access calls.
     * You provide token_type in the access token generation call to an authorization server.
     * <p>
     * It is make as class instead of {@link Enum} to make it easy to create a new {@link TokenType} that can be used by client.
     * A good example of custom {@link TokenType} is <a href="https://en.wikipedia.org/wiki/Message_authentication_code">MAC </a>
     * </p>
     *
     * <p>
     * if you choose <a href="https://en.wikipedia.org/wiki/Message_authentication_code">MAC </a> and sign_type (default hmac-sha-1 on most implementation),
     * the access token is generated and kept as secret in Key Manager as an attribute,
     * and an encrypted secret is sent back as access_token.
     * </p>
     * <p>
     * By now, Cello supports only {@link #BEARER} token type.
     * But new {@link TokenType} can be easily added by implementing the {@link Oauth2AccessTokenGenerator}
     * </p>
     *
     * @param typeName - type name that represent this token type
     */
    public record TokenType(@NotNull String typeName) {

        public TokenType {
            Assert.notNull(typeName, "Access token type name should not be null!");
        }

        public static TokenType create(@NotNull String typeName) {
            return new TokenType(typeName);
        }

        /**
         * If you choose Bearer (default on most implementation), an access_token is generated and sent back to you.
         * Bearer can be simply understood as "give access to the bearer of this token." One valid token and no question asked.
         */
        public static final TokenType BEARER = TokenType.create("Bearer");
    }
}
