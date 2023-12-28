package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import static com.odeyalo.sonata.cello.core.Oauth2ErrorCode.INVALID_CLIENT;
import static org.assertj.core.api.Assertions.*;

class Oauth2AuthorizationRequestValidationExceptionHandlingStrategyTest {
    Oauth2AuthorizationRequestValidationExceptionHandlingStrategy testable = new Oauth2AuthorizationRequestValidationExceptionHandlingStrategy();

    @Test
    void shouldReturnTrueIfSupportsThisTypeOfException() {
        testable.supports(Oauth2AuthorizationRequestValidationException.errorCodeOnly(INVALID_CLIENT))
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseIfSupportsThisTypeOfException() {
        testable.supports(new IllegalArgumentException())
                .as(StepVerifier::create)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

    @Test
    void shouldReturn400BadRequestStatusIfClientIdIsInvalid() {
        var invalidClientIdException = Oauth2AuthorizationRequestValidationException.errorCodeOnly(INVALID_CLIENT);
        MockServerWebExchange mockExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.handle(mockExchange,invalidClientIdException).block();

        assertThat(mockExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}