package com.odeyalo.sonata.cello.client.registration;

import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

import java.util.List;

/**
 * Tests for {@link InMemoryOauth2RegisteredClientService}
 */
public class InMemoryOauth2RegisteredClientServiceTest {

    @Test
    void shouldFoundClientIfSingleClientRegisteredAndExistingClientInProvided() {
        var existingClient = Oauth2RegisteredClientFaker.create().get();

        var testable = new InMemoryOauth2RegisteredClientService(
                existingClient
        );

        testable.findByClientId(existingClient.getCredentials().getClientId())
                .as(StepVerifier::create)
                .expectNext(existingClient)
                .verifyComplete();
    }

    @Test
    void shouldFoundClientIfMultipleClientsRegisteredAndExistingClientInProvided() {
        var existingClient1 = Oauth2RegisteredClientFaker.create().get();
        var existingClient2 = Oauth2RegisteredClientFaker.create().get();

        var testable = new InMemoryOauth2RegisteredClientService(
                List.of(existingClient1, existingClient2)
        );

        testable.findByClientId(existingClient1.getCredentials().getClientId())
                .as(StepVerifier::create)
                .expectNext(existingClient1)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfClientIdNotExist() {
        var testable = new InMemoryOauth2RegisteredClientService();

        testable.findByClientId("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}