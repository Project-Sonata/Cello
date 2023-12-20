package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable representation of resource owner
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class ResourceOwner {
    @NotNull
    String principal;
    @Nullable
    Object credentials;
    @NotNull
    @Builder.Default
    ResourceOwnerInfo ownerInfo = EmptyResourceOwnerInfo.instance();
    // Scopes available for this resource owner(example, read_profile, playlist:read, etc)
    @NotNull
    @Builder.Default
    ScopeContainer availableScopes = ScopeContainer.empty();

    public static ResourceOwner withPrincipalOnly(String principal) {
        return builder().principal(principal).build();
    }
}
