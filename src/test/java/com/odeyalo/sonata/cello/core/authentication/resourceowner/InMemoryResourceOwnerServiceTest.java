package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryResourceOwnerServiceTest {

    @Test
    void shouldReturnResourceOwnerIfUsernameExist() {
        ResourceOwner expectedOwner = ResourceOwner.withPrincipalOnly("odeyalo");
        InMemoryResourceOwnerService testable = new InMemoryResourceOwnerService(List.of(
                expectedOwner
        ));

        testable.loadResourceOwnerByUsername("odeyalo")
                .as(StepVerifier::create)
                .expectNext(expectedOwner)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfUsernameNotExist() {
        ResourceOwner expectedOwner = ResourceOwner.withPrincipalOnly("odeyalo");
        InMemoryResourceOwnerService testable = new InMemoryResourceOwnerService(List.of(
                expectedOwner
        ));

        testable.loadResourceOwnerByUsername("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}