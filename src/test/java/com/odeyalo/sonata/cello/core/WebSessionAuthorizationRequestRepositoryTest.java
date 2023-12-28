package com.odeyalo.sonata.cello.core;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

class WebSessionAuthorizationRequestRepositoryTest {

    @Test
    void shouldSaveRequest() {
        WebSessionAuthorizationRequestRepository testable = new WebSessionAuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .expectNext(mockRequest)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfAuthorizationRequestWasNotSavedPreviously() {
        WebSessionAuthorizationRequestRepository testable = new WebSessionAuthorizationRequestRepository();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveExistingAuthorizationRequest() {
        WebSessionAuthorizationRequestRepository testable = new WebSessionAuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.removeAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}