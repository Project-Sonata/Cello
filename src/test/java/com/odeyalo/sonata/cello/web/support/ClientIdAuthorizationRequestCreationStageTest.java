package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.CLIENT_ID;

class ClientIdAuthorizationRequestCreationStageTest {

    @Test
    void shouldAddClientIdToAuthorizationRequestIfClientIdIsProvided() {
        var testable = new ClientIdAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize")
                .queryParam(CLIENT_ID, "miku")
                .build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectNextMatches(b -> Objects.equals(b.build().getClientId(), "miku"))
                .verifyComplete();

    }

    @Test
    void shouldThrowExceptionIfClientIdNotPresent() {
        var testable = new ClientIdAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize").build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectError(AuthorizationRequestCreationException.class)
                .verify();

    }
}