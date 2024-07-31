package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.ClaimsWrapperFactory;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * Represent a successful access token response as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-5.1">Successful response</a>
 */
public interface AccessTokenResponse {
    /**
     * REQUIRED.  The access token issued by the authorization server.
     *
     * @return an access token value
     */
    @NotNull
    String getAccessTokenValue();

    /**
     * REQUIRED.  The type of the token issued as described in
     * Section 7.1.  Value is case insensitive.
     *
     * @return a type of this token
     */
    @NotNull
    String getTokenType();

    /**
     * RECOMMENDED.  The lifetime in seconds of the access token.  For
     * example, the value "3600" denotes that the access token will
     * expire in one hour from the time the response was generated.
     * If omitted, the authorization server SHOULD provide the
     * expiration time via other means or document the default value.
     *
     * @return time after which the token will expire, in seconds. If null is returned,
     * then a token has a default expiration time
     */
    @Nullable
    Integer getExpiresIn();

    /**
     * OPTIONAL.  The refresh token, which can be used to obtain new
     * access tokens using the same authorization grant as described
     * in Section 6.
     *
     * @return a refresh token
     */
    @Nullable
    String getRefreshToken();

    /**
     * OPTIONAL, if identical to the scope requested by the client;
     * otherwise, REQUIRED.  The scope of the access token as
     * described by Section 3.3.
     *
     * @return scopes wrapped in {@link ScopeContainer}
     */
    @NotNull
    ScopeContainer getScopes();

    /**
     * @return immutable key-value pairs that should be included in response
     */
    @NotNull
    Parameters getParameters();

    /**
     * @return - grant type that this response represent
     */
    @NotNull
    GrantType getGrantType();

    /**
     * The parameters are included in the entity-body of the HTTP response
     * using the "application/json" media type. The
     * parameters are serialized into a JavaScript Object Notation (JSON)
     * structure by adding each parameter at the highest structure level.
     * Parameter names and string values are included as JSON strings.
     * Numerical values are included as JSON numbers.  The order of
     * parameters does not matter and can vary.
     */
    @Value
    @Builder
    class Parameters {
        @NotNull
        @Singular("put")
        Map<String, Object> source;

        public static Parameters empty() {
            return new Parameters(Collections.emptyMap());
        }

        public static Parameters fromMap(@NotNull final Map<String, Object> source) {
            return new Parameters(source);
        }

        @NotNull
        public <T> T wrap(@NotNull final ClaimsWrapperFactory<T> wrapperFactory) {
            return wrapperFactory.create(source);
        }

        public int size() {
            return source.size();
        }

        public boolean isEmpty() {
            return source.isEmpty();
        }

        public boolean containsKey(final String key) {
            return source.containsKey(key);
        }

        public Object get(final String key) {
            return source.get(key);
        }

        @NotNull
        public Map<String, Object> asImmutableMap() {
            return Collections.unmodifiableMap(source);
        }
    }
}
