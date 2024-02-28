package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static reactor.core.publisher.Mono.defer;

/**
 * Uses the username-password authentication schema to authenticate the user, if username or password is invalid, then {@link #AUTHENTICATION_EXCEPTION} is being thrown
 */
public final class UsernamePasswordResourceOwnerAuthenticationProvider implements ResourceOwnerAuthenticationProvider {
    private final ResourceOwnerService resourceOwnerService;

    private static final Mono<UsernamePasswordAuthenticatedResourceOwnerAuthentication> AUTHENTICATION_EXCEPTION = defer(() -> Mono.error(
            ResourceOwnerAuthenticationException.withCustomMessage("Failed to authenticate the user. Username or password mismatch")
    ));

    public UsernamePasswordResourceOwnerAuthenticationProvider(ResourceOwnerService resourceOwnerService) {
        this.resourceOwnerService = resourceOwnerService;
    }

    @Override
    @NotNull
    public Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull final AuthenticationCredentials credentials) {
        if ( !(credentials instanceof final UsernamePasswordAuthenticationCredentials usernamePassword) ) {
            return Mono.empty();
        }

        return authenticateUserOrError(usernamePassword.getUsername(), usernamePassword.getPassword())
                .map(it -> it);
    }

    @NotNull
    private Mono<UsernamePasswordAuthenticatedResourceOwnerAuthentication> authenticateUserOrError(@NotNull String username,
                                                                                                   @NotNull String password) {
        return resourceOwnerService.loadResourceOwnerByUsername(username)
                .filter(resourceOwner -> isCredentialsValid(resourceOwner, password))
                .map(resourceOwner -> UsernamePasswordAuthenticatedResourceOwnerAuthentication.create(username, password, resourceOwner))
                .switchIfEmpty(AUTHENTICATION_EXCEPTION);
    }

    private boolean isCredentialsValid(@NotNull ResourceOwner resourceOwner, @NotNull String password) {
        return Objects.equals(resourceOwner.getCredentials(), password);
    }
}
