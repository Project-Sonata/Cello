package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.stream.Stream;

class FormDataUsernamePasswordAuthenticationCredentialsConverterTest {

    @Test
    void shouldReturnCredentialsOfSpecificType() {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&password=password")
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(it -> it instanceof UsernamePasswordAuthenticationCredentials)
                .verifyComplete();
    }

    @ParameterizedTest
    @ValueSource(strings = {"username123", "i_love_Miku", "i_wish_I_was-a-cloud"})
    void shouldReturnCredentialsWithCorrectUsername(String username) {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(String.format("username=%s&password=password", username))
        );

        testable.convertToCredentials(exchange)
                .cast(UsernamePasswordAuthenticationCredentials.class)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(username, it.getUsername()))
                .verifyComplete();
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "i_love_Miku", "i_wish_I_was-a-cloud"})
    void shouldReturnCredentialsWithCorrectPassword(String password) {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(String.format("username=username&password=%s", password))
        );

        testable.convertToCredentials(exchange)
                .cast(UsernamePasswordAuthenticationCredentials.class)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(password, it.getPassword()))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoIfUsernameParameterIsNotPresent() {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("password=odeyalo")
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoIfPasswordParameterIsNotPresent() {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo")
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoIfFormDataIsEmpty() {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @ParameterizedTest
    @MethodSource(value = "unsupportedContentTypes")
    void shouldReturnEmptyMonoIfRequestNotSupported(MediaType mediaType) {
        final var testable = new FormDataUsernamePasswordAuthenticationCredentialsConverter();
        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    public static Stream<Arguments> unsupportedContentTypes() {
        return Stream.of(
                Arguments.of(MediaType.APPLICATION_JSON),
                Arguments.of(MediaType.TEXT_HTML)
        );
    }
}