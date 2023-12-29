package com.odeyalo.sonata.cello.core;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

class WebSessionOauth2AuthorizationRequestRepositoryTest {

    @Test
    void shouldSaveRequest() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
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
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveExistingAuthorizationRequest() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
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