package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.spring.configuration.RegisterOauth2Clients;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
class AuthorizeEndpointTest {

    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

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

            responseSpec.expectStatus().isFound();
        }

        @Test
        void shouldReturn400BadRequestIfResponseTypeNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
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
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdIsNotExist() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, "not_exist")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfRedirectUriIsNotRegistered() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, "http:localhost:9812/redirect/not/allowed")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
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
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
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