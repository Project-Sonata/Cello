package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.List;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.REDIRECT_URI;
import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.SCOPE;
import static org.junit.jupiter.api.Assertions.*;

class ScopeAuthorizationRequestCreationStageTest {

    @Test
    void shouldAddScopesToAuthorizationRequestIfScopeIsPresent() {
        var testable = new ScopeAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize")
                .queryParam(SCOPE, "read write profile")
                .build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectNextMatches(b -> b.build().getScopes().containsAll(
                        List.of(
                                SimpleScope.withName("write"),
                                SimpleScope.withName("read"),
                                SimpleScope.withName("profile")
                        )))
                .verifyComplete();

    }

    @Test
    void shouldThrowExceptionIfScopeParamNotPresent() {
        var testable = new ScopeAuthorizationRequestCreationStage();

        var mockRequest = MockServerHttpRequest.get("/authorize").build();
        var serverWebExchange = MockServerWebExchange.from(mockRequest);
        var builder = AuthorizationRequest.builder();

        testable.processCreation(serverWebExchange, builder)
                .as(StepVerifier::create)
                .expectError(AuthorizationRequestCreationException.class)
                .verify();
    }
}