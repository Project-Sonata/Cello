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

public interface AuthorizationCodeGenerator {
    /**
     * Generate a new {@link GeneratedAuthorizationCode} that later can be exchanged for an access token
     *
     * @param generationContext - an additional info that associated with this authorization code
     * @return = {@link Mono} with {@link GeneratedAuthorizationCode}
     */
    @NotNull
    Mono<GeneratedAuthorizationCode> newAuthorizationCode(@NotNull AuthorizationCodeService.AuthorizationCodeGenerationContext generationContext);

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
