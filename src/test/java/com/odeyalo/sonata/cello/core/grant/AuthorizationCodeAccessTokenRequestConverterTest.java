package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.exception.MalformedAccessTokenRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationCodeAccessTokenRequestConverterTest {

    @Test
    void shouldReturnNothingIfGrantTypeIsNotAuthorizationCode() {
        final AuthorizationCodeAccessTokenRequestConverter testable = new AuthorizationCodeAccessTokenRequestConverter();

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("grant_type=password&username=123")
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnCodeFromRequestForm() {
        final AuthorizationCodeAccessTokenRequestConverter testable = new AuthorizationCodeAccessTokenRequestConverter();

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("grant_type=authorization_code&code=my_code&redirect_uri=http://localhost:3000/callback")
        );

        testable.convert(exchange)
                .cast(AuthorizationCodeAccessTokenRequest.class)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getAuthorizationCode()).isEqualTo("my_code"))
                .verifyComplete();
    }

    @Test
    void shouldReturnRedirectUriFromRequestForm() {
        final AuthorizationCodeAccessTokenRequestConverter testable = new AuthorizationCodeAccessTokenRequestConverter();

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("grant_type=authorization_code&code=my_code&redirect_uri=http://localhost:3000/callback")
        );

        testable.convert(exchange)
                .cast(AuthorizationCodeAccessTokenRequest.class)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getRedirectUri()).isEqualTo(RedirectUri.create("http://localhost:3000/callback")))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfCodeParameterIsNotPresent() {
        final AuthorizationCodeAccessTokenRequestConverter testable = new AuthorizationCodeAccessTokenRequestConverter();

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("grant_type=authorization_code&redirect_uri=http://localhost:3000/callback")
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectError(MalformedAccessTokenRequestException.class)
                .verify();
    }

    @Test
    void shouldReturnErrorIfRedirectUriIsMissing() {
        final AuthorizationCodeAccessTokenRequestConverter testable = new AuthorizationCodeAccessTokenRequestConverter();

        final var exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/hello")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body("grant_type=authorization_code&code=123")
        );

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectError(MalformedAccessTokenRequestException.class)
                .verify();
    }
}