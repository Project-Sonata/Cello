package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;
import testing.factory.AuthenticationCredentialsConverters;
import testing.factory.ResourceOwnerAuthenticationProviders;

import java.util.Objects;

class ProviderDelegateResourceOwnerAuthenticationManagerTest {

    @Test
    void shouldReturnEmptyMonoIfConverterReturnedNothing() {
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        final var expectedResourceOwner = createResourceOwner("username", "password");

        final var testable = new ProviderDelegateResourceOwnerAuthenticationManager(
                AuthenticationCredentialsConverters.usernamePassword(),
                ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner));

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticationOnValidCredentials() {
        final String username = "odeyalo";
        final String password = "password";

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(String.format("username=%s&password=%s", username, password))
        );

        final var expectedResourceOwner = createResourceOwner(username, password);

        final var testable = new ProviderDelegateResourceOwnerAuthenticationManager(
                AuthenticationCredentialsConverters.usernamePassword(),
                ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner)
        );

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(owner -> Objects.equals(owner.getResourceOwner(), expectedResourceOwner))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticationExceptionOnInvalidCredentials() {
        final String username = "odeyalo";
        final String password = "password";

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(String.format("username=%s&password=invalid", username))
        );

        final var expectedResourceOwner = createResourceOwner(username, password);

        final var testable = new ProviderDelegateResourceOwnerAuthenticationManager(
                AuthenticationCredentialsConverters.usernamePassword(),
                ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner)
        );

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }

    private static ResourceOwner createResourceOwner(String username, String password) {
        return ResourceOwner.builder()
                .principal(username)
                .credentials(password)
                .availableScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("read_profile"),
                        SimpleScope.withName("read_playlist"),
                        SimpleScope.withName("write_playlist")
                ))
                .build();
    }
}