package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.DefaultOauth2AccessTokenResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultOauth2AccessTokenResponseHandlerTest {
    static final MockWebServer mockWebServer = new MockWebServer();

    static final Logger logger = LoggerFactory.getLogger(DefaultOauth2AccessTokenResponseHandlerTest.class);

    static final String VALID_ACCESS_TOKEN_EXCHANGE_JSON = """
            {
              "access_token": "example-access-token",
              "token_type": "Bearer",
              "expires_in": 3600
            }
            """;

    static final String VALID_REFRESH_TOKEN_AWARE_ACCESS_TOKEN_EXCHANGE_JSON = """
            {
              "access_token": "example-access-token",
              "token_type": "Bearer",
              "refresh_token": "refresh-token-here",
              "expires_in": 3600
            }
            """;

 static final String VALID_SCOPES_AWARE_TOKEN_AWARE_ACCESS_TOKEN_EXCHANGE_JSON = """
            {
              "access_token": "example-access-token",
              "token_type": "Bearer",
              "scope": "miku nakano",
              "expires_in": 3600
            }
            """;

    static final String INVALID_AUTHORIZATION_CODE_ERROR_JSON = """
            {
                "error": "invalid_grant",
                "error_description": "Authorization code is invalid"
            }
            """;

    static WebClient webClient;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer.start();

        String baseUrl = "http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort();
        webClient = WebClient.create(baseUrl);

        logger.info("Will send requests to: " + baseUrl);
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.close();
    }

    @Test
    void shouldReturnAccessTokenValueThatWasInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );
        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getAccessTokenValue()).isEqualTo("example-access-token"))
                .verifyComplete();
    }

    @Test
    void shouldReturnAccessTokenExpiresInThatWasInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getExpiresIn()).isEqualTo(3600))
                .verifyComplete();
    }

    @Test
    void shouldReturnAccessTokenTypeInThatWasInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getTokenType()).isEqualTo("Bearer"))
                .verifyComplete();
    }

    @Test
    void shouldReturnNullRefreshTokenIfWasMissingInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getRefreshTokenValue()).isNull())
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyScopesIfWasMissingInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getScopes()).isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldReturnRefreshTokenValueIfWasPresentInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_REFRESH_TOKEN_AWARE_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getRefreshTokenValue()).isEqualTo("refresh-token-here"))
                .verifyComplete();
    }

    @Test
    void shouldReturnScopesThatWasInResponse() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_SCOPES_AWARE_TOKEN_AWARE_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getScopes()).containsExactlyInAnyOrder(
                        SimpleScope.withName("miku"),
                        SimpleScope.withName("nakano")))
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfAuthorizationServerReturnedError() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody(INVALID_AUTHORIZATION_CODE_ERROR_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        // using real response instead of mocked one to boost performance and make tests less fragile for changes
        final WebClient.ResponseSpec responseSpec = webClient.post().uri("/token").retrieve();

        final var testable = new DefaultOauth2AccessTokenResponseHandler(
                new DefaultOauth2AccessTokenResponse.Factory()
        );

        testable.handleResponse(responseSpec)
                .as(StepVerifier::create)
                .expectError(AuthorizationCodeExchangeException.class)
                .verify();

    }
}