package com.odeyalo.sonata.cello.core.token.access;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Generate opaque token, save it to {@link AccessTokenStore} and return it.
 */
@Component
public class OpaquePersistentOauth2AccessTokenGenerator implements Oauth2AccessTokenGenerator {
    private final AccessTokenStore accessTokenStore;
    private final OpaqueOauth2AccessTokenValueGenerator tokenValueGenerator;
    private static final int ONE_HOUR = 1;

    @Autowired
    public OpaquePersistentOauth2AccessTokenGenerator(AccessTokenStore accessTokenStore,
                                                      OpaqueOauth2AccessTokenValueGenerator tokenValueGenerator) {
        this.accessTokenStore = accessTokenStore;
        this.tokenValueGenerator = tokenValueGenerator;
    }

    @Override
    @NotNull
    public Mono<Oauth2AccessToken> generateToken(@NotNull Oauth2AccessTokenGenerationContext context) {
        return tokenValueGenerator.generateTokenValue()
                .flatMap(tokenValue -> {

                    Oauth2AccessToken accessToken = buildAccessToken(context, tokenValue);

                    return accessTokenStore.saveToken(accessToken)
                            .thenReturn(accessToken);
                });
    }

    @NotNull
    private static Oauth2AccessToken buildAccessToken(@NotNull Oauth2AccessTokenGenerationContext context,
                                                      @NotNull String tokenValue) {

        Instant issuedAt = Instant.now();
        Instant expiresIn = issuedAt.plus(ONE_HOUR, ChronoUnit.HOURS);

        return Oauth2AccessToken.builder()
                .tokenValue(tokenValue)
                .issuedAt(issuedAt)
                .expiresIn(expiresIn)
                .scopes(context.getScopes())
                .build();
    }
}
