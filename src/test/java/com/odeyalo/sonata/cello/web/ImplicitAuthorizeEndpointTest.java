package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.DefaultOauth2ResponseTypes;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.net.URISyntaxException;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for implicit response type only
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
public class ImplicitAuthorizeEndpointTest {
    public static final String STATE_PARAMETER_VALUE = "opaque";
    public static final String EXISTING_CLIENT_ID = "123";
    @Autowired
    WebTestClient webTestClient;

    final String AUTHENTICATION_COOKIE_NAME = "clid";
    final String AUTHENTICATION_COOKIE_VALUE = "odeyalo";


    @Test
    void shouldRedirectToProvidedRedirectUriAsResponse() throws URISyntaxException {
        WebTestClient.ResponseSpec responseSpec = sendValidImplicitAuthorizeRequest();

        responseSpec.expectStatus().isFound();

        HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

        assertThat(headers).containsKey(HttpHeaders.LOCATION);

        URI uri = URI.create(headers.getFirst(HttpHeaders.LOCATION));

        assertThat(
                new URI(uri.getScheme(),
                        uri.getAuthority(),
                        uri.getPath(),
                        null, // Ignore the query part of the input url
                        uri.getFragment())
                        .toString()
        ).isEqualTo("http://localhost:4000");
    }

    @Test
    void shouldReturnAccessTokenInRedirectUri() {
        WebTestClient.ResponseSpec responseSpec = sendValidImplicitAuthorizeRequest();

        HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

        assertThat(headers).containsKey(HttpHeaders.LOCATION);

        String locationUri = headers.getFirst(HttpHeaders.LOCATION);

        assertThat(locationUri).isNotNull();
        assertThat(URI.create(locationUri)).hasParameter("access_token");
    }

    @Test
    void shouldReturnAccessTokenTypeInRedirectUri() {
        WebTestClient.ResponseSpec responseSpec = sendValidImplicitAuthorizeRequest();

        HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

        assertThat(headers).containsKey(HttpHeaders.LOCATION);

        String locationHeaderValue = headers.getFirst(HttpHeaders.LOCATION);

        assertThat(locationHeaderValue).isNotNull();
        assertThat(URI.create(locationHeaderValue)).hasParameter("token_type", "Bearer");
    }

    @Test
    void shouldReturnAccessTokenExpiresTimeInRedirectUri() {
        WebTestClient.ResponseSpec responseSpec = sendValidImplicitAuthorizeRequest();

        HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

        assertThat(headers).containsKey(HttpHeaders.LOCATION);

        String locationUri = headers.getFirst(HttpHeaders.LOCATION);

        assertThat(locationUri).isNotNull();
        assertThat(URI.create(locationUri)).hasParameter("expires_in", "3600");
    }

    @Test
    void shouldReturnStateAsProvidedInRedirectUri() {
        WebTestClient.ResponseSpec responseSpec = sendValidImplicitAuthorizeRequest();

        HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

        assertThat(headers).containsKey(HttpHeaders.LOCATION);

        String locationUri = headers.getFirst(HttpHeaders.LOCATION);

        assertThat(locationUri).isNotNull();
        assertThat(URI.create(locationUri)).hasParameter("state", STATE_PARAMETER_VALUE);
    }

    @NotNull
    private WebTestClient.ResponseSpec sendValidImplicitAuthorizeRequest() {
        return webTestClient.get()
                .uri(builder ->
                        builder
                                .path("/authorize")
                                .queryParam(RESPONSE_TYPE, DefaultOauth2ResponseTypes.IMPLICIT.getName())
                                .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                .queryParam(REDIRECT_URI, "http://localhost:4000")
                                .queryParam(SCOPE, "read write")
                                .queryParam(STATE, "opaque")
                                .build())
                .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                .exchange();
    }
}
