package com.odeyalo.sonata.cello.core;

import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

class WebSessionOauth2AuthorizationRequestRepositoryTest {

    @Test
    void shouldReturnIllegalStateExceptionIfTheAttributeDoesNotPresentedInRequest() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void shouldCompletedSuccessfullyIfAttributeIsPresented() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "123");

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldSaveRequestForTheGivenFlow() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "flow_123");

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .expectNext(mockRequest)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyAuthorizationRequestIfRequestIsNotAssociatedWithTheFlow() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "flow_123");

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        MockServerWebExchange secondExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        secondExchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "not_exist_flow");

        testable.loadAuthorizationRequest(secondExchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationRequestAssociatedWithSpecificFlowId() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest1 = MockOauth2AuthorizationRequest.create();
        Oauth2AuthorizationRequest mockRequest2 = MockOauth2AuthorizationRequest.create();

        MockServerWebExchange exchange1 = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        MockServerWebExchange exchange2 = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        exchange1.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "flow_123");
        exchange2.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "second_flow");

        testable.saveAuthorizationRequest(mockRequest1, exchange1)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.saveAuthorizationRequest(mockRequest2, exchange2)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange1)
                .as(StepVerifier::create)
                .expectNext(mockRequest1)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange2)
                .as(StepVerifier::create)
                .expectNext(mockRequest2)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfAuthorizationRequestWasNotSavedPreviously() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "123");

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRemoveExistingAuthorizationRequestAssociatedWithFlow() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "123");

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

    @Test
    void shouldDeleteNothingIfAuthorizationRequestIsNotAssociatedWithFlow() {
        WebSessionOauth2AuthorizationRequestRepository testable = new WebSessionOauth2AuthorizationRequestRepository();
        Oauth2AuthorizationRequest mockRequest = MockOauth2AuthorizationRequest.create();

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "1234");

        MockServerWebExchange invalidFlowIdExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        invalidFlowIdExchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "1234");

        testable.saveAuthorizationRequest(mockRequest, exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.removeAuthorizationRequest(invalidFlowIdExchange)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.loadAuthorizationRequest(exchange)
                .as(StepVerifier::create)
                .expectNext(mockRequest)
                .verifyComplete();
    }
}