package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import testing.UriUtils;
import testing.WithAuthenticatedResourceOwner;
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
@WithAuthenticatedResourceOwner
public class ImplicitAuthorizeEndpointTest {
    public static final String STATE_PARAMETER_VALUE = "opaque";
    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;

    String currentFlowId;
    String currentSessionId;

    @BeforeEach
    void prepare() {
        prepareAuthorizationFlow();
    }

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
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("action", "approved");
        formData.add("flow_id", currentFlowId);

        return webTestClient.post()
                .uri("/oauth2/consent")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .cookie("SESSION", currentSessionId)
                .exchange();
    }

    private void prepareAuthorizationFlow() {
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri(builder ->
                        builder
                                .path("/authorize")
                                .queryParam(RESPONSE_TYPE, "token")
                                .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                .queryParam(SCOPE, "read write")
                                .queryParam(STATE, "opaque")
                                .build())
                .exchange();

        exchange.expectStatus().isFound();

        FluxExchangeResult<String> result = exchange
                .returnResult(String.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionId = result.getResponseCookies().getFirst("SESSION");

        currentFlowId = UriUtils.parseQueryParameters(uri).get("flow_id");
        currentSessionId = sessionId.getValue();
    }
}
