package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCode;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchange;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchangeException;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.MockAuthorizationCodeExchange;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.TestingAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.TestingAuthenticationCredentials;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Oauth2ProviderResourceOwnerAuthenticationProviderTest {

    @Test
    void shouldReturnNothingIfUnsupportedCredentialsAreUsed() {

        final var testable = new Oauth2ProviderResourceOwnerAuthenticationProvider(
                MockAuthorizationCodeExchange.willReturnStaticAccessToken("ignored"),
                new NullConverter()
        );

        final var credentials = new TestingAuthenticationCredentials();

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticationIfAuthorizationCodeIsValid() {

        final var expectedAuthenticationResult = TestingAuthenticatedResourceOwnerAuthentication.random();

        final var testable = new Oauth2ProviderResourceOwnerAuthenticationProvider(
                MockAuthorizationCodeExchange.willReturnStaticAccessToken("valid_access_token"),
                StubConverter.willAlwaysReturn(expectedAuthenticationResult)
        );

        testable.attemptAuthentication(new Oauth2ProviderAuthorizationCodeAuthenticationCredentials("google", "testing"))
                .as(StepVerifier::create)
                .expectNext(expectedAuthenticationResult)
                .verifyComplete();
    }

    @Test
    void shouldReturnExceptionIfAuthorizationCodeCantBeExchangedForAccessToken() {

        final var testable = new Oauth2ProviderResourceOwnerAuthenticationProvider(
                ThrowableAuthorizationCodeExchange.willAlwaysThrow(new AuthorizationCodeExchangeException()),
                new NullConverter()
        );

        testable.attemptAuthentication(new Oauth2ProviderAuthorizationCodeAuthenticationCredentials("google", "testing"))
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }

    @Test
    void shouldReturnExceptionIfProviderIsNotSupported() {

        final var testable = new Oauth2ProviderResourceOwnerAuthenticationProvider(
                DummyAuthorizationCodeExchanger.instance(),
                new NullConverter()
        );

        testable.attemptAuthentication(new Oauth2ProviderAuthorizationCodeAuthenticationCredentials("unsupported", "testing"))
                .as(StepVerifier::create)
                .expectError(NotSupportedOauth2ProviderException.class)
                .verify();
    }

    private record StubConverter(
            AuthenticatedResourceOwnerAuthentication stub) implements Oauth2AccessTokenResponseAuthenticationConverter {

        public static StubConverter willAlwaysReturn(AuthenticatedResourceOwnerAuthentication toReturn) {
            return new StubConverter(toReturn);
        }

        @Override
        public @NotNull Mono<AuthenticatedResourceOwnerAuthentication> convertToAuthentication(@NotNull final Oauth2AccessTokenResponse response) {
            if ( response.getAccessTokenValue().equals("valid_access_token") ) {
                return Mono.just(stub);
            }
            return Mono.empty();
        }
    }

    private record ThrowableAuthorizationCodeExchange(Throwable error) implements AuthorizationCodeExchange {

        public static AuthorizationCodeExchange willAlwaysThrow(final Throwable ex) {
            return new ThrowableAuthorizationCodeExchange(ex);
        }

        @Override
        public @NotNull Mono<Oauth2AccessTokenResponse> exchange(@NotNull final AuthorizationCode code) {
            return Mono.error(error);
        }

        @Override
        public boolean supports(final String providerName) {
            return true;
        }
    }

    private static class DummyAuthorizationCodeExchanger implements AuthorizationCodeExchange {

        public static AuthorizationCodeExchange instance() {
            return new DummyAuthorizationCodeExchanger();
        }

        @Override
        public @NotNull Mono<Oauth2AccessTokenResponse> exchange(@NotNull final AuthorizationCode code) {
            return Mono.empty();
        }

        @Override
        public boolean supports(final String providerName) {
            return false;
        }
    }

    private record NullConverter() implements Oauth2AccessTokenResponseAuthenticationConverter {

        @Override
        public @NotNull Mono<AuthenticatedResourceOwnerAuthentication> convertToAuthentication(@NotNull final Oauth2AccessTokenResponse response) {
            return Mono.empty();
        }
    }
}