package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Provider to handle specific type of authentication credentials.
 * @see ProviderDelegateResourceOwnerAuthenticationManager
 * @see AuthenticationCredentials
 */
public interface ResourceOwnerAuthenticationProvider {
    /**
     * Attempt the authentication with the given credentials
     * @param credentials - credentials to use, in most cases it is {@link AuthenticationCredentials} resolved using {@link AuthenticationCredentialsConverter}
     * @return - {@link Mono} with {@link AuthenticatedResourceOwnerAuthentication} on success(credentials are valid, user account not expired, any rule),
     * empty {@link Mono} if implementation does not support this type of {@link AuthenticationCredentials}
     *
     * @throws ResourceOwnerAuthenticationException if credentials are invalid or authentication processing failed(expired account, validation rule violated, etc.)
     */
    @NotNull
    Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull AuthenticationCredentials credentials);

}
