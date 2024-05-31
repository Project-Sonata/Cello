package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.DefaultOauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public final class MockAuthorizationCodeExchange implements AuthorizationCodeExchange {
    private final String accessTokenValue;

    public MockAuthorizationCodeExchange(final String accessTokenValue) {
        this.accessTokenValue = accessTokenValue;
    }

    public static MockAuthorizationCodeExchange willReturnStaticAccessToken(String value) {
        return new MockAuthorizationCodeExchange(value);
    }

    @Override
    public @NotNull Mono<Oauth2AccessTokenResponse> exchange(@NotNull final AuthorizationCode code) {
        return Mono.just(
                DefaultOauth2AccessTokenResponse.builder()
                        .accessTokenValue(accessTokenValue)
                        .expiresIn(3600)
                        .tokenType("Bearer")
                        .build()
        );
    }

    @Override
    public boolean supports(final String providerName) {
        return true;
    }
}
