package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.stream.Stream;

class UsernamePasswordResourceOwnerAuthenticationManagerTest {

    public static Stream<Arguments> unsupportedContentTypes() {
        return Stream.of(
                Arguments.of(MediaType.APPLICATION_JSON),
                Arguments.of(MediaType.TEXT_HTML)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "unsupportedContentTypes")
    void shouldReturnEmptyMonoIfContentTypeNotMatch(MediaType mediaType) {
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
        );

        var testable = new UsernamePasswordResourceOwnerAuthenticationManager(username -> Mono.empty());

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoIfParamNotPresent() {
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&something=password")
        );

        var testable = new UsernamePasswordResourceOwnerAuthenticationManager(username -> Mono.empty());

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthenticationOnValidCredentials() {
        String username = "odeyalo";
        String password = "password";

        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&password=password")
        );

        ResourceOwner expectedResourceOwner = ResourceOwner.builder()
                .principal(username)
                .credentials(password)
                .availableScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("read_profile"),
                        SimpleScope.withName("read_playlist"),
                        SimpleScope.withName("write_playlist")
                ))
                .build();

        var testable = new UsernamePasswordResourceOwnerAuthenticationManager(name -> Mono.just(
                expectedResourceOwner
        ));

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(owner ->
                        Objects.equals(owner.getResourceOwner(), expectedResourceOwner) &&
                        Objects.equals(owner.getPrincipal(), username) &&
                        Objects.equals(owner.getCredentials(), password)
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnExceptionIfUserNotFound() {
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&password=password")
        );

        var testable = new UsernamePasswordResourceOwnerAuthenticationManager(name -> Mono.empty());

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }
    @Test
    void shouldReturnExceptionIfPasswordInvalid() {
        var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("username=odeyalo&password=password")
        );

        var testable = new UsernamePasswordResourceOwnerAuthenticationManager(username -> Mono.just(
                ResourceOwner.builder()
                        .principal(username)
                        .credentials("miku")
                        .build()
        ));

        testable.attemptAuthentication(exchange)
                .as(StepVerifier::create)
                .expectError(ResourceOwnerAuthenticationException.class)
                .verify();
    }
}