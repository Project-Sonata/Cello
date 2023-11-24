package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.REDIRECT_URI;
import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.STATE;
import static org.junit.jupiter.api.Assertions.*;

class RedirectUriAuthorizationRequestCreationStageTest {

    @Test
    void shouldAddRedirectUriToAuthorizationRequestIfPresent() {
        var testable = new RedirectUriAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize")
                .queryParam(REDIRECT_URI, "http://localhost:3000")
                .build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectNextMatches(b -> Objects.equals(b.build().getRedirectUri(), "http://localhost:3000"))
                .verifyComplete();
    }

    @Test
    void shouldThrowExceptionIfRedirectUriNotPresent() {
        var testable = new RedirectUriAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize").build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectError(AuthorizationRequestCreationException.class)
                .verify();
    }
}