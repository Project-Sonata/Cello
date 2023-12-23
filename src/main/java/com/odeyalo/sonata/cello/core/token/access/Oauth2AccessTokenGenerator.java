package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.exception.Oauth2AccessTokenGenerationException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Generate the {@link Oauth2AccessToken} that can be used by Oauth2-Client to access protected resources.
 */
public interface Oauth2AccessTokenGenerator {
    /**
     * Generate the access token for Oauth2 Client
     * @param context - context that can be used to create the {@link Oauth2AccessToken}
     * @return - {@link Mono} with generated {@link Oauth2AccessToken}, or {@link Mono} with error if token can't be generated
     *
     * @throws Oauth2AccessTokenGenerationException - if {@link Oauth2AccessToken} cannot be generated
     */
    @NotNull
    Mono<Oauth2AccessToken> generateToken(@NotNull Oauth2AccessTokenGenerationContext context);
}
