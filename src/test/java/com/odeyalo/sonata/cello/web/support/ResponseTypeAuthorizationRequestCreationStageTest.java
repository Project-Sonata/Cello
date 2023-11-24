package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.RESPONSE_TYPE;

class ResponseTypeAuthorizationRequestCreationStageTest {

    @Test
    void shouldAddResponseTypeIfRequestContain() {
        var testable = new ResponseTypeAuthorizationRequestCreationStage();
        var mockRequest = MockServerHttpRequest.get("/authorize")
                .queryParam(RESPONSE_TYPE, "token")
                .build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectNextMatches(b -> Objects.equals(b.build().getResponseType(), "token"))
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorIfResponseTypeNotPresent() {
        var testable = new ResponseTypeAuthorizationRequestCreationStage();
        var mockRequest = MockServerHttpRequest.get("/authorize").build();

        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectError(AuthorizationRequestCreationException.class)
                .verify();
    }
}