package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeClaims;
import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Generate a new {@link GeneratedAuthorizationCode} that can be exchanged for access token as described in
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-1.3.1">Authorization code</a>
 */
public interface AuthorizationCodeGenerator {
    /**
     * Generate a new {@link GeneratedAuthorizationCode} that can be exchanged for access token
     *
     * @param generationContext - an additional info that associated with this authorization code
     * @return = {@link Mono} with {@link GeneratedAuthorizationCode}
     */
    @NotNull
    Mono<GeneratedAuthorizationCode> newAuthorizationCode(@NotNull AuthorizationCodeGenerationContext generationContext);

    /**
     * Context for generating an authorization code
     */
    @Value
    @Builder
    class AuthorizationCodeGenerationContext {
        @NotNull
        Oauth2RegisteredClient grantedFor;
        @NotNull
        ScopeContainer requestedScopes;
        @NotNull
        ResourceOwner grantedBy;
        @NotNull
        @Builder.Default
        AuthorizationCodeClaims claims = AuthorizationCodeClaims.empty();
    }
}
