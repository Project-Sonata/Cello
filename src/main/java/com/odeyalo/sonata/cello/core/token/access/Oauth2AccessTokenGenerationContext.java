package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Additional info that can be used to generate {@link Oauth2AccessToken}
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class Oauth2AccessTokenGenerationContext {
    /**
     * Represent the client that requested generation of {@link Oauth2AccessToken}
     */
    @NotNull
    Oauth2RegisteredClient client;
    /**
     * Scopes requested by the client
     */
    @NotNull
    @Builder.Default
    ScopeContainer scopes = ScopeContainer.empty();
    /**
     * Resource owner that has approved generation of {@link Oauth2AccessToken} for the
     * {@link #client}
     */
    @NotNull
    ResourceOwner resourceOwner;
}
