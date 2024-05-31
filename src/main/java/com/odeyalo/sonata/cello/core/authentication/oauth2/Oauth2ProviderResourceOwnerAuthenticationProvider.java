package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCode;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchange;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticationCredentials;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of {@link ResourceOwnerAuthenticationProvider} that supports Oauth 2.0 protocol
 * and used to convert the received authorization code to {@link AuthenticatedResourceOwnerAuthentication}
 * <p>
 * Received authorization code is exchanged to access token, that then converted to {@link AuthenticatedResourceOwnerAuthentication} by {@link Oauth2AccessTokenResponseAuthenticationConverter}
 * </p>
 *
 * @see AuthenticatedResourceOwnerAuthentication
 * @see ResourceOwnerAuthenticationProvider
 * @see Oauth2ProviderAuthorizationCodeAuthenticationCredentials
 */
@Component
public final class Oauth2ProviderResourceOwnerAuthenticationProvider implements ResourceOwnerAuthenticationProvider {
    private final List<AuthorizationCodeExchange> codeExchangers;
    private final Oauth2AccessTokenResponseAuthenticationConverter authenticationConverter;


    public Oauth2ProviderResourceOwnerAuthenticationProvider(final AuthorizationCodeExchange exchanger,
                                                             final Oauth2AccessTokenResponseAuthenticationConverter authenticationConverter) {
        this.codeExchangers = List.of(exchanger);
        this.authenticationConverter = authenticationConverter;
    }

    @Autowired
    public Oauth2ProviderResourceOwnerAuthenticationProvider(final List<AuthorizationCodeExchange> exchangers,
                                                             final Oauth2AccessTokenResponseAuthenticationConverter authenticationConverter) {
        Assert.notEmpty(exchangers, "At least one authorization code exchanger must present!");
        this.codeExchangers = exchangers;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    @NotNull
    public Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull AuthenticationCredentials credentials) {

        if ( !(credentials instanceof Oauth2ProviderAuthorizationCodeAuthenticationCredentials authorizationCodeCreds) ) {
            return Mono.empty();
        }

        final String authorizationCode = authorizationCodeCreds.getAuthorizationCode();
        final String provider = authorizationCodeCreds.getProviderName();

        return Flux.fromIterable(codeExchangers)
                .filter(exchanger -> exchanger.supports(provider))
                .next()
                .flatMap(exchanger -> exchanger.exchange(AuthorizationCode.wrapString(authorizationCode)))
                .flatMap(authenticationConverter::convertToAuthentication)
                .onErrorMap(err -> resourceOwnerAuthenticationException(authorizationCode, provider, err))
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        NotSupportedOauth2ProviderException.withCustomMessage("OAuth 2.0 provider: %s is not supported", provider)
                )));
    }

    @NotNull
    private static ResourceOwnerAuthenticationException resourceOwnerAuthenticationException(final String authorizationCode,
                                                                                             final String provider,
                                                                                             final Throwable err) {
        return ResourceOwnerAuthenticationException
                .withMessageAndCause(
                        String.format("Failed to authenticate the user using OAuth 2.0 Provider: '%s' using authorization code: '%s'", provider, authorizationCode),
                        err
                );
    }
}
