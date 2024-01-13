package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.exception.MalformedOauth2RequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

class ImplicitOauth2AuthorizationRequestConverterTest {

    @ParameterizedTest
    @ValueSource(strings = {"code", "custom_one"})
    void shouldReturnEmptyOnNonTokenResponseType(String responseType) {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/hello")
                        .queryParam("response_type", responseType)
                        .queryParam("client_id", "123")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("state", "mikuiloveyou")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldNotReturnAnyExceptionIfRequestValid() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "123")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("state", "mikuiloveyou")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldReturnSpecificAuthorizationRequest() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "123")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("state", "mikuiloveyou")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> request instanceof ImplicitOauth2AuthorizationRequest)
                .verifyComplete();
    }

    @Test
    void shouldReturnClientIdEqualToProvided() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("state", "mikuiloveyou")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;
                    return Objects.equals(implicitRequest.getClientId(), "mikuloverid");
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnStateEqualToProvided() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("state", "mikuiloveyou")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;
                    return Objects.equals(implicitRequest.getState(), "mikuiloveyou");
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnNullStateIfNotProvided() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;
                    return Objects.isNull(implicitRequest.getState());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnRedirectUriEqualToProvided() {
        String redirectUri = "http://localhost:8080/oauth2/cello/callback";

        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("redirect_uri", redirectUri)
                        .queryParam("state", "mystate")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;
                    return Objects.equals(implicitRequest.getRedirectUri().uriString(), redirectUri);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfNullRedirectUriWasNotProvided() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("state", "mystate")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectError(MalformedOauth2RequestException.class)
                .verify();
    }

    @Test
    void shouldReturnAllScopesProvidedInRequest() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("state", "mystate")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .queryParam("scope", "write read delete")
                        .build()
        );

        List<Scope> expectedScopes = List.of(
                SimpleScope.withName("write"),
                SimpleScope.withName("read"),
                SimpleScope.withName("delete")
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;

                    return implicitRequest.getScopes().containsAll(expectedScopes);

                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyScopesIfScopeWasNotProvided() {
        ImplicitOauth2AuthorizationRequestConverter testable = new ImplicitOauth2AuthorizationRequestConverter();

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/authorize")
                        .queryParam("response_type", "token")
                        .queryParam("client_id", "mikuloverid")
                        .queryParam("state", "mystate")
                        .queryParam("redirect_uri", "http://localhost:8080/oauth2/cello/callback")
                        .build()
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(request -> {
                    ImplicitOauth2AuthorizationRequest implicitRequest = (ImplicitOauth2AuthorizationRequest) request;

                    return implicitRequest.getScopes().isEmpty();
                })
                .verifyComplete();
    }
}