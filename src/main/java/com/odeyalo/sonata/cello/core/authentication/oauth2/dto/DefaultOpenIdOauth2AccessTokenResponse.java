package com.odeyalo.sonata.cello.core.authentication.oauth2.dto;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@Builder
@AllArgsConstructor
public class DefaultOpenIdOauth2AccessTokenResponse implements OpenIdOauth2AccessTokenResponse {
    @NotNull
    String accessTokenValue;
    int expiresIn;
    @NotNull
    String tokenType;
    @Nullable
    String refreshTokenValue;
    @NotNull
    ScopeContainer scopes;
    @NotNull
    String idToken;

    private DefaultOpenIdOauth2AccessTokenResponse(@NotNull final Oauth2AccessTokenResponse copyFrom,
                                                   @NotNull final String idToken) {
        this.accessTokenValue = copyFrom.getAccessTokenValue();
        this.tokenType = copyFrom.getTokenType();
        this.expiresIn = copyFrom.getExpiresIn();
        this.refreshTokenValue = copyFrom.getRefreshTokenValue();
        this.scopes = copyFrom.getScopes();
        this.idToken = idToken;
    }

    @NotNull
    public static DefaultOpenIdOauth2AccessTokenResponse fromExisting(@NotNull final Oauth2AccessTokenResponse copyFrom,
                                                                      @NotNull final String idToken) {

        return new DefaultOpenIdOauth2AccessTokenResponse(copyFrom, idToken);
    }
}
