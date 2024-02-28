package testing.factory;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordResourceOwnerAuthenticationProvider;

/**
 * Factory to create {@link com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationProvider}
 */
public final class ResourceOwnerAuthenticationProviders {

    public static UsernamePasswordResourceOwnerAuthenticationProvider usernamePassword(ResourceOwner... availableOwners) {
        return new UsernamePasswordResourceOwnerAuthenticationProvider(
                ResourceOwnerServices.withResourceOwners(availableOwners)
        );
    }
}
