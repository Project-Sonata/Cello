package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class InMemoryOauth2AuthenticationMetadataRepositoryTest {

    @Test
    void shouldReturnSavedValue() {
        final InMemoryOauth2AuthenticationMetadataRepository testable = new InMemoryOauth2AuthenticationMetadataRepository();

        final Oauth2AuthenticationMetadata authenticationMetadata = Oauth2AuthenticationMetadata.withFlowId("flow_123");

        testable.save("123", authenticationMetadata)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findBy("123")
                .as(StepVerifier::create)
                .expectNext(authenticationMetadata)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingForNotExistingValue() {
        final InMemoryOauth2AuthenticationMetadataRepository testable = new InMemoryOauth2AuthenticationMetadataRepository();

        final Oauth2AuthenticationMetadata authenticationMetadata = Oauth2AuthenticationMetadata.withFlowId("flow_123");

        testable.save("123", authenticationMetadata)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findBy("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnRemovedValue() {
        final InMemoryOauth2AuthenticationMetadataRepository testable = new InMemoryOauth2AuthenticationMetadataRepository();

        final Oauth2AuthenticationMetadata authenticationMetadata = Oauth2AuthenticationMetadata.withFlowId("flow_123");

        testable.save("123", authenticationMetadata)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.remove("123")
                .as(StepVerifier::create)
                .expectNext(authenticationMetadata)
                .verifyComplete();
    }

    @Test
    void shouldRemoveValueFromRepository() {
        final InMemoryOauth2AuthenticationMetadataRepository testable = new InMemoryOauth2AuthenticationMetadataRepository();

        final Oauth2AuthenticationMetadata authenticationMetadata = Oauth2AuthenticationMetadata.withFlowId("flow_123");

        testable.save("123", authenticationMetadata)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.remove("123")
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        testable.findBy("123")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfIdIsNotAssociatedWithValue() {
        final InMemoryOauth2AuthenticationMetadataRepository testable = new InMemoryOauth2AuthenticationMetadataRepository();

        final Oauth2AuthenticationMetadata authenticationMetadata = Oauth2AuthenticationMetadata.withFlowId("flow_123");

        testable.save("123", authenticationMetadata)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.remove("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}