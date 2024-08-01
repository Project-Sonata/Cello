package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Contains a metadata that associated with some authorization code
 */
@Value
@AllArgsConstructor(staticName = "from")
@Builder
public class AuthorizationCodeMetadata {
    @NotNull
    ResourceOwner grantedBy;
    @NotNull
    Oauth2RegisteredClient grantedFor;
    @NotNull
    ScopeContainer requestedScopes;
    @NotNull
    @Builder.Default
    AuthorizationCodeClaims claims = AuthorizationCodeClaims.empty();
}
