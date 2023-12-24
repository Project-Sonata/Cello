package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Returns predefined {@link Oauth2AccessToken}
 */
public class MockOauth2AccessTokenGenerator implements Oauth2AccessTokenGenerator{
    private final Oauth2AccessToken oauth2AccessToken;

    public MockOauth2AccessTokenGenerator(Oauth2AccessToken oauth2AccessToken) {
        this.oauth2AccessToken = oauth2AccessToken;
    }

    public MockOauth2AccessTokenGenerator(String tokenValue) {
        this.oauth2AccessToken = Oauth2AccessToken.builder()
                .tokenValue(tokenValue)
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .scopes(ScopeContainer.empty())
                .build();
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessToken> generateToken(@NotNull Oauth2AccessTokenGenerationContext context) {
        return Mono.just(oauth2AccessToken);
    }
}
