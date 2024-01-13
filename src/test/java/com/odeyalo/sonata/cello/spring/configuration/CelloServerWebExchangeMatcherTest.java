package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.spring.configuration.security.CelloServerWebExchangeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import reactor.test.StepVerifier;

class CelloServerWebExchangeMatcherTest {


    @Test
    void shouldReturnTrueIfUriStartsWithOauth2() {
        CelloServerWebExchangeMatcher testable = new CelloServerWebExchangeMatcher();
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/authorize").build()
        );
        testable.matches(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(ServerWebExchangeMatcher.MatchResult::isMatch)
                .verifyComplete();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/hello", "/oauth/hello", "/miku"
    })
    void shouldReturnFalseIfUriStartsWithOauth2(String uri) {
        CelloServerWebExchangeMatcher testable = new CelloServerWebExchangeMatcher();
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get(uri).build()
        );

        testable.matches(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(matchResult -> !matchResult.isMatch())
                .verifyComplete();
    }
}