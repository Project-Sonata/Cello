package testing.factory;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.InMemoryResourceOwnerService;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerService;

import java.util.List;

/**
 * Factory to create different {@link ResourceOwnerService}
 */
public final class ResourceOwnerServices {

    public static ResourceOwnerService withResourceOwners(ResourceOwner... owners) {
        return new InMemoryResourceOwnerService(List.of(owners));
    }
}
