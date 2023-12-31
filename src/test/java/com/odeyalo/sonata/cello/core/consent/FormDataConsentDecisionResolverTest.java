package com.odeyalo.sonata.cello.core.consent;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

class FormDataConsentDecisionResolverTest {

    @Test
    void shouldReturnApprovedIfActionInFormDataIsApproved() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("action=approved")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.APPROVED)
                .verifyComplete();
    }

    @Test
    void shouldReturnApprovedIfActionInFormDataIsEqualToCustom() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();
        testable.setApprovedValue("test");

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("action=test")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.APPROVED)
                .verifyComplete();
    }

    @Test
    void shouldReturnApprovedIfKeyInFormDataIsEqualToCustom() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();
        testable.setConsentDecisionFormDataKey("test");

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("test=approved")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.APPROVED)
                .verifyComplete();
    }

    @Test
    void shouldReturnApprovedIfKeyInFormDataAndValueIsEqualToCustom() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();
        testable.setConsentDecisionFormDataKey("test");
        testable.setApprovedValue("odeyalo");

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("test=odeyalo")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.APPROVED)
                .verifyComplete();
    }

    @Test
    void shouldReturnDeniedIfActionInFormDataIsDenied() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("action=denied")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.DENIED)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfActionNotPresent() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnDeniedIfActionIsPresentedButMalformed() {
        FormDataConsentDecisionResolver testable = new FormDataConsentDecisionResolver();

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body("action=malformed")
        );

        testable.resolveConsentDecision(webExchange)
                .as(StepVerifier::create)
                .expectNextMatches(consentDecision -> consentDecision.decision() == ConsentDecision.Decision.DENIED)
                .verifyComplete();
    }
}