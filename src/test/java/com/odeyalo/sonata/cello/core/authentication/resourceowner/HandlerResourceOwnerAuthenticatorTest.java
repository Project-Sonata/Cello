package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

class HandlerResourceOwnerAuthenticatorTest {

    @Test
    void shouldSetCookiesOnAuthenticationSuccess() {
        var authenticationManager = new TestingAuthenticationManager();

        var successHandler = new TestingSuccessHandler();

        var failureHandler = new TestingFailureHandler();

        var testable = new HandlerResourceOwnerAuthenticator(authenticationManager, successHandler, failureHandler);

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/login")
                .body("username=test&password=test"));

        testable.authenticate(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(response -> Objects.equals(response.getStatusCode(), HttpStatus.OK) &&
                        response.getCookies().containsKey("test"))
                .verifyComplete();
    }

    @Test
    void shouldReturnBadRequestOnFailed() {
        ResourceOwnerAuthenticationManager authenticationManager = (exchange) -> Mono.error(new ResourceOwnerAuthenticationException("Bad credentials"));

        var successHandler = new TestingSuccessHandler();

        var failureHandler = new TestingFailureHandler();

        var testable = new HandlerResourceOwnerAuthenticator(authenticationManager, successHandler, failureHandler);

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/login")
                .body("username=test&password=test"));

        testable.authenticate(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(response -> Objects.equals(response.getStatusCode(), HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    private static class TestingAuthenticationManager implements ResourceOwnerAuthenticationManager {

        @Override
        public @NotNull Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull ServerWebExchange webExchange) {
            return Mono.just(
                    new UsernamePasswordAuthenticatedResourceOwnerAuthentication("test", "test", ResourceOwner.withPrincipalOnly("test"))
            );
        }
    }

    private static class TestingSuccessHandler implements ResourceOwnerAuthenticationSuccessHandler {

        @Override
        public @NotNull Mono<Void> onAuthenticationSuccess(@NotNull ServerWebExchange exchange, @NotNull AuthenticatedResourceOwnerAuthentication authentication) {
            return Mono.fromRunnable(() -> {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                exchange.getResponse().addCookie(ResponseCookie.from("test", "test").build());
            });
        }
    }

    private static class TestingFailureHandler implements ResourceOwnerAuthenticationFailureHandler {

        @Override
        public @NotNull Mono<Void> onAuthenticationFailure(@NotNull ServerWebExchange exchange, @NotNull ResourceOwnerAuthenticationException authenticationException) {
            return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST));
        }
    }

}
