package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderAuthorizationCodeAuthenticationCredentials;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * {@link  AuthenticationCredentialsConverter} that converts a {@link ServerWebExchange} to {@link Oauth2ProviderAuthorizationCodeAuthenticationCredentials}.
 * Represent Authorization Code grant type to exchange authorization code to access token
 */
public final class DefaultOauth2ProviderAuthenticationCredentialsConverter implements AuthenticationCredentialsConverter {

    private final CallbackOauth2ProviderNameResolver providerNameResolver;

    public DefaultOauth2ProviderAuthenticationCredentialsConverter(CallbackOauth2ProviderNameResolver providerNameResolver) {
        this.providerNameResolver = providerNameResolver;
    }

    @Override
    @NotNull
    public Mono<AuthenticationCredentials> convertToCredentials(@NotNull final ServerWebExchange exchange) {
        final String authorizationCode = exchange.getRequest().getQueryParams().getFirst("code");

        if ( StringUtils.isEmpty(authorizationCode) ) {
            return Mono.empty();
        }

        return providerNameResolver.resolveProviderName(exchange)
                .map(it -> Oauth2ProviderAuthorizationCodeAuthenticationCredentials.builder()
                        .providerName(it)
                        .authorizationCode(authorizationCode)
                        .build()
                );
    }
}
