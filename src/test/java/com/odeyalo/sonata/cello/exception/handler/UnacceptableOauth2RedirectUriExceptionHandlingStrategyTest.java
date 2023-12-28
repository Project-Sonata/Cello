package com.odeyalo.sonata.cello.exception.handler;

import com.odeyalo.sonata.cello.exception.UnacceptableOauth2RedirectUriException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class UnacceptableOauth2RedirectUriExceptionHandlingStrategyTest {
    UnacceptableOauth2RedirectUriExceptionHandlingStrategy testable = new UnacceptableOauth2RedirectUriExceptionHandlingStrategy();

    @Test
    void shouldReturnTrueIfSupportsThisTypeOfException() {
        testable.supports(new UnacceptableOauth2RedirectUriException())
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
    void shouldReturn400BadRequest() {
        MockServerWebExchange mockExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        testable.handle(mockExchange, new UnacceptableOauth2RedirectUriException()).block();

        assertThat(mockExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
}