package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class AuthorizeEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    final String AUTHENTICATION_COOKIE_NAME = "clid";
    final String AUTHENTICATION_COOKIE_VALUE = "odeyalo";

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ValidRequestTest {

        @Test
        void shouldReturnOkStatus() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectStatus().isOk();
        }

        @Test
        void shouldReturnHtmlContentType() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectHeader().contentType(MediaType.TEXT_HTML);
        }

        @Test
        void shouldReturn400BadRequestIfResponseTypeNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, "123")
                                    .queryParam(REDIRECT_URI, "http://localhost:4000")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, "http://localhost:4000")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfRedirectUriNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(CLIENT_ID, "123")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfScopeNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(CLIENT_ID, "123")
                                    .queryParam(REDIRECT_URI, "http://localhost:4000")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfStateNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(CLIENT_ID, "123")
                                    .queryParam(REDIRECT_URI, "http://localhost:4000")
                                    .queryParam(SCOPE, "read write")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @NotNull
        private WebTestClient.ResponseSpec sendValidAuthorizeRequest() {
            return webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "code")
                                    .queryParam(CLIENT_ID, "123")
                                    .queryParam(REDIRECT_URI, "http://localhost:4000")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class MalformedRedirectUriRequestTest {

    }
}