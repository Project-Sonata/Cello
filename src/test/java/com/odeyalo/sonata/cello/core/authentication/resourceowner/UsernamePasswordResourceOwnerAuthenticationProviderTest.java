package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.factory.ResourceOwnerAuthenticationProviders;

import java.util.Objects;

class UsernamePasswordResourceOwnerAuthenticationProviderTest {

    @Test
    void shouldReturnAuthenticatedUserWithCorrectResourceOwnerOnValidCredentials() {
        final var expectedResourceOwner = createResourceOwner();
        final var credentials = UsernamePasswordAuthenticationCredentials.from("odeyalo", "password");

        final var testable = ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner);

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(it.getResourceOwner(), expectedResourceOwner))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticatedUserWithCorrectPrincipalOnValidCredentials() {
        final var expectedResourceOwner = createResourceOwner();
        final var credentials = UsernamePasswordAuthenticationCredentials.from("odeyalo", "password");

        final var testable = ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner);

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(it.getPrincipal(), expectedResourceOwner.getPrincipal()))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticatedUserWithCorrectCredentialsOnValidCredentials() {
        final var expectedResourceOwner = createResourceOwner();
        final var credentials = UsernamePasswordAuthenticationCredentials.from("odeyalo", "password");

        final var testable = ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner);

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(it.getCredentials(), expectedResourceOwner.getCredentials()))
                .verifyComplete();
    }

    @Test
    void shouldReturnExceptionIfInvalidCredentialsAreUsed() {
        final var expectedResourceOwner = createResourceOwner();
        final var credentials = UsernamePasswordAuthenticationCredentials.from("odeyalo", "invalid");

        final var testable = ResourceOwnerAuthenticationProviders.usernamePassword(expectedResourceOwner);

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }

    @Test
    void shouldReturnEmptyMonoOnNonSupportedAuthenticationCredentials() {
        final var resourceOwner = createResourceOwner();
        final var credentials = TestingAuthenticationCredentials.create();

        final var testable = ResourceOwnerAuthenticationProviders.usernamePassword(resourceOwner);

        testable.attemptAuthentication(credentials)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private static ResourceOwner createResourceOwner() {
        return ResourceOwner.builder()
                .principal("odeyalo")
                .credentials("password")
                .availableScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("read_profile"),
                        SimpleScope.withName("read_playlist"),
                        SimpleScope.withName("write_playlist")
                ))
                .build();
    }
}