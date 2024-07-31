package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@SuperBuilder
public abstract class AbstractAccessTokenResponse implements AccessTokenResponse {
    @NotNull
    String accessTokenValue;
    @NotNull
    String tokenType;
    @NotNull
    Integer expiresIn;
    @Nullable
    String refreshToken;
    @NotNull
    ScopeContainer scopes;
    @NotNull
    @Builder.Default
    Parameters parameters = Parameters.empty();
    @NotNull
    GrantType grantType;
}
