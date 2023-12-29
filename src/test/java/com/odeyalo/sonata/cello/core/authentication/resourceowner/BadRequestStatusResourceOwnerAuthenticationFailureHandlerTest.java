package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import static org.assertj.core.api.Assertions.assertThat;

class BadRequestStatusResourceOwnerAuthenticationFailureHandlerTest {

    @Test
    void shouldReturnBadRequest() {
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());

        BadRequestStatusResourceOwnerAuthenticationFailureHandler testable = new BadRequestStatusResourceOwnerAuthenticationFailureHandler();

        testable.onAuthenticationFailure(exchange, new ResourceOwnerAuthenticationException()).block();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}