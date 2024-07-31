package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Central interface that used to create and validate the authorization codes that used to exchange for access tokens.
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-1.3.1">Authorization code</a>
 */
public interface AuthorizationCodeService extends AuthorizationCodeGenerator, AuthorizationCodeLoader {
    /**
     * Generate a new {@link GeneratedAuthorizationCode} that can be exchanged for access token,
     * a token after generation MUST BE capable to be loaded using {@link #loadUsing(String)} method
     *
     * @param generationContext - an additional info that associated with this authorization code
     * @return = {@link Mono} with {@link GeneratedAuthorizationCode}
     */
    @NotNull
    @Override
    Mono<GeneratedAuthorizationCode> newAuthorizationCode(@NotNull AuthorizationCodeService.AuthorizationCodeGenerationContext generationContext);
}
