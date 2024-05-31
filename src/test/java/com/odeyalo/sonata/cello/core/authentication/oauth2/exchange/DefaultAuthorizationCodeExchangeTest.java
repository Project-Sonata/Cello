package com.odeyalo.sonata.cello.core.authentication.oauth2.exchange;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.DefaultOauth2AccessTokenResponse;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import testing.faker.Oauth2ProviderRegistrationFaker;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultAuthorizationCodeExchangeTest {
    static final MockWebServer mockWebServer = new MockWebServer();
    static final Logger logger = LoggerFactory.getLogger(DefaultAuthorizationCodeExchangeTest.class);

    static String tokenEndpoint;

    static final String VALID_ACCESS_TOKEN_EXCHANGE_JSON = """
            {
              "access_token": "example-access-token",
              "token_type": "Bearer",
              "expires_in": 3600
            }
            """;

    static WebClient webClient;

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer.start();

        String baseUrl = "http://" + mockWebServer.getHostName() + ":" + mockWebServer.getPort();

        webClient = WebClient.create();

        tokenEndpoint = baseUrl + "/token";

        logger.info("Will send requests to: " + baseUrl);
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.close();
    }

    @Test
    void shouldSendCorrectClientId() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        final Oauth2ProviderRegistration testRegistration = Oauth2ProviderRegistrationFaker.create()
                .withClientId("odeyalooo")
                .withTokenEndpoint(tokenEndpoint)
                .get();

        final var testable = new DefaultAuthorizationCodeExchange(
                testRegistration,
                webClient,
                new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory())
        );

        testable.exchange(AuthorizationCode.wrapString("MikuNakano"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        final HttpUrl requestUrl = takeRequestOrError();
        assertThat(requestUrl.queryParameter("client_id")).isEqualTo("odeyalooo");
    }

    @Test
    void shouldSendCorrectClientSecret() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        final Oauth2ProviderRegistration testRegistration = Oauth2ProviderRegistrationFaker.create()
                .withClientSecret("miku")
                .withTokenEndpoint(tokenEndpoint)
                .get();

        final var testable = new DefaultAuthorizationCodeExchange(
                testRegistration,
                webClient,
                new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory())
        );

        testable.exchange(AuthorizationCode.wrapString("MikuNakano"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        final HttpUrl requestUrl = takeRequestOrError();

        assertThat(requestUrl.queryParameter("client_secret")).isEqualTo("miku");
    }

    @Test
    void shouldSendCorrectAuthorizationCode() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        final Oauth2ProviderRegistration testRegistration = Oauth2ProviderRegistrationFaker.create()
                .withTokenEndpoint(tokenEndpoint)
                .get();

        final var testable = new DefaultAuthorizationCodeExchange(
                testRegistration,
                webClient,
                new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory())
        );

        testable.exchange(AuthorizationCode.wrapString("MikuNakano"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        final HttpUrl requestUrl = takeRequestOrError();

        assertThat(requestUrl.queryParameter("code")).isEqualTo("MikuNakano");
    }

    @Test
    void shouldSendCorrectRedirectUri() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        final Oauth2ProviderRegistration testRegistration = Oauth2ProviderRegistrationFaker.create()
                .withRedirectUri("http://localhost:3000/callback")
                .withTokenEndpoint(tokenEndpoint)
                .get();

        final var testable = new DefaultAuthorizationCodeExchange(
                testRegistration,
                webClient,
                new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory())
        );

        testable.exchange(AuthorizationCode.wrapString("MikuNakano"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        final HttpUrl requestUrl = takeRequestOrError();

        assertThat(requestUrl.queryParameter("redirect_uri")).isEqualTo("http://localhost:3000/callback");
    }

    @Test
    void shouldSendAuthorizationCodeGrantType() throws InterruptedException {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(VALID_ACCESS_TOKEN_EXCHANGE_JSON)
                        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        final Oauth2ProviderRegistration testRegistration = Oauth2ProviderRegistrationFaker.create()
                .withTokenEndpoint(tokenEndpoint)
                .get();

        final var testable = new DefaultAuthorizationCodeExchange(
                testRegistration,
                webClient,
                new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory())
        );

        testable.exchange(AuthorizationCode.wrapString("MikuNakano"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        final HttpUrl requestUrl = takeRequestOrError();

        assertThat(requestUrl.queryParameter("grant_type")).isEqualTo("authorization_code");
    }

    @NotNull
    private static HttpUrl takeRequestOrError() throws InterruptedException {
        final RecordedRequest recordedRequest = mockWebServer.takeRequest(5, TimeUnit.SECONDS);
        assertThat(recordedRequest).as("Request must be sent to Oauth2 provider!").isNotNull();

        final HttpUrl requestUrl = recordedRequest.getRequestUrl();

        assertThat(requestUrl).isNotNull();
        return requestUrl;
    }

}