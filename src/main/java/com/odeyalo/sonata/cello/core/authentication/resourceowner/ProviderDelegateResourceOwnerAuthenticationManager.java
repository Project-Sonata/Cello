package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Implementation that just delegates the authentication process to {@link ResourceOwnerAuthenticationProvider}
 */
public final class ProviderDelegateResourceOwnerAuthenticationManager implements ResourceOwnerAuthenticationManager {
    private final AuthenticationCredentialsConverter authenticationCredentialsConverter;
    private final ResourceOwnerAuthenticationProvider authenticationProvider;

    public ProviderDelegateResourceOwnerAuthenticationManager(AuthenticationCredentialsConverter authenticationCredentialsConverter,
                                                              ResourceOwnerAuthenticationProvider authenticationProvider) {
        this.authenticationCredentialsConverter = authenticationCredentialsConverter;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    @NotNull
    public Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull ServerWebExchange webExchange) {

        return authenticationCredentialsConverter.convertToCredentials(webExchange)
                .flatMap(authenticationProvider::attemptAuthentication);
    }
}
