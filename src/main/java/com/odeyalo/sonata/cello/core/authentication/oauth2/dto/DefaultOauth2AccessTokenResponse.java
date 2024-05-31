package com.odeyalo.sonata.cello.core.authentication.oauth2.dto;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.odeyalo.sonata.cello.core.Oauth2TokenExchangeResponseParameters.*;

/**
 * Default Oauth2 response that follows Oauth 2.0 Standard.
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#autoid-40">Access Token Response</a>
 */
@Value
@Builder
public class DefaultOauth2AccessTokenResponse implements Oauth2AccessTokenResponse {
    @NotNull
    String accessTokenValue;
    int expiresIn;
    @NotNull
    String tokenType;
    @Nullable
    String refreshTokenValue;
    @NotNull
    @Builder.Default
    ScopeContainer scopes = ScopeContainer.empty();

    public static final class Factory implements Oauth2AccessTokenResponse.Factory {

        @Override
        @NotNull
        public Oauth2AccessTokenResponse create(@NotNull final Map<String, Object> body) {
            final var scope = (String) body.get(SCOPE);

            final var scopes = ScopeContainer.fromOauth2String(scope);

            final var tokenResponse = DefaultOauth2AccessTokenResponse.builder()
                    .accessTokenValue((String) body.get(ACCESS_TOKEN))
                    .expiresIn((int) body.get(EXPIRES_IN))
                    .tokenType((String) body.get(TOKEN_TYPE))
                    .scopes(scopes)
                    .refreshTokenValue((String) body.get(REFRESH_TOKEN))
                    .build();

            if ( body.containsKey(ID_TOKEN) ) {
                final var idToken = (String) body.get(ID_TOKEN);

                return DefaultOpenIdOauth2AccessTokenResponse.fromExisting(tokenResponse, idToken);
            }

            return tokenResponse;
        }
    }
}
