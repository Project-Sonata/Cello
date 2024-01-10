package testing;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

/**
 * Creates the {@link UsernamePasswordAuthenticatedResourceOwnerAuthentication} with provided values from {@link WithAuthenticatedResourceOwner} annotation
 */
public class WithAuthenticatedResourceOwnerSecurityContextFactory implements WithSecurityContextFactory<WithAuthenticatedResourceOwner> {
    private final Logger logger = LoggerFactory.getLogger(WithAuthenticatedResourceOwnerSecurityContextFactory.class);

    @Override
    @NotNull
    public SecurityContext createSecurityContext(@NotNull WithAuthenticatedResourceOwner annotation) {

        logger.info("Creating UsernamePasswordAuthenticatedResourceOwnerAuthentication from: {}", annotation);

        var authentication = UsernamePasswordAuthenticatedResourceOwnerAuthentication.builder()
                .principal(annotation.username())
                .credentials(annotation.password())
                .resourceOwner(createResourceOwner(annotation))
                .build();

        return new SecurityContextImpl(authentication);
    }

    private static ResourceOwner createResourceOwner(@NotNull WithAuthenticatedResourceOwner annotation) {

        ScopeContainer availableScopes = ScopeContainer.fromCollection(
                Arrays.stream(annotation.authorities())
                        .map(SimpleScope::withName)
                        .toList()
        );

        return ResourceOwner.builder()
                .principal(annotation.username())
                .credentials(annotation.password())
                .availableScopes(availableScopes)
                .build();
    }
}
