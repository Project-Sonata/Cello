package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationManager;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import testing.UriUtils;
import testing.spring.configuration.RegisterOauth2Clients;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
public class ResourceOwnerLoginEndpointTest {
    private static final String VALID_USERNAME = "odeyalo";
    private static final String VALID_PASSWORD = "password";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    public static final String EXISTING_CLIENT_ID = "123";
    public static final String EXISTING_REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager;

    String currentFlowId;
    String currentSessionId;


    @BeforeEach
    void setUp() {
        WebTestClient.ResponseSpec exchange = webTestClient.get()
                .uri(builder ->
                        builder
                                .path("/authorize")
                                .queryParam(RESPONSE_TYPE, "token")
                                .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                .queryParam(REDIRECT_URI, EXISTING_REDIRECT_URI)
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

    @Test
    void shouldReturnRedirectIfLoginSuccess() {

        when(resourceOwnerAuthenticationManager.attemptAuthentication(any()))
                .thenReturn(
                        Mono.just(
                                new UsernamePasswordAuthenticatedResourceOwnerAuthentication
                                        (VALID_USERNAME, VALID_PASSWORD, ResourceOwner.withPrincipalOnly(VALID_PASSWORD)))
                );

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_KEY, VALID_USERNAME);
        formData.add(PASSWORD_KEY, VALID_PASSWORD);

        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(formData);

        responseSpec.expectStatus().isFound();
    }

    @Test
    void shouldReturnRedirectToConsentPage() throws URISyntaxException {

        when(resourceOwnerAuthenticationManager.attemptAuthentication(any()))
                .thenReturn(
                        Mono.just(
                                new UsernamePasswordAuthenticatedResourceOwnerAuthentication
                                        (VALID_USERNAME, VALID_PASSWORD, ResourceOwner.withPrincipalOnly(VALID_PASSWORD)))
                );

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_KEY, VALID_USERNAME);
        formData.add(PASSWORD_KEY, VALID_PASSWORD);

        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(formData);

        HttpHeaders responseHeaders = responseSpec.returnResult(String.class).getResponseHeaders();

        URI uri = responseHeaders.getLocation();

        assertThat(uri).isNotNull();

        assertThat(
                new URI(uri.getScheme(),
                        uri.getAuthority(),
                        uri.getPath(),
                        null, // Ignore the query part of the input url
                        uri.getFragment())
                        .toString()
        ).isEqualTo("/oauth2/consent");
    }

    @Test
    void shouldReturnRedirectToConsentPageWithFlowIdQueryParameter() throws UnsupportedEncodingException {

        when(resourceOwnerAuthenticationManager.attemptAuthentication(any()))
                .thenReturn(
                        Mono.just(
                                new UsernamePasswordAuthenticatedResourceOwnerAuthentication
                                        (VALID_USERNAME, VALID_PASSWORD, ResourceOwner.withPrincipalOnly(VALID_PASSWORD)))
                );

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_KEY, VALID_USERNAME);
        formData.add(PASSWORD_KEY, VALID_PASSWORD);

        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(formData);

        HttpHeaders responseHeaders = responseSpec.returnResult(String.class).getResponseHeaders();

        URI uri = responseHeaders.getLocation();

        assertThat(uri).isNotNull();

        assertThat(UriUtils.parseQueryParameters(uri).get("flow_id"))
                .isNotEmpty();
    }

    @Test
    void shouldReturnBadRequestStatusIfCredentialsInvalid() {

        when(resourceOwnerAuthenticationManager.attemptAuthentication(any()))
                .thenReturn(
                        Mono.error(
                                ResourceOwnerAuthenticationException.withCustomMessage("Resource owner credentials is invalid")
                        ));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_KEY, VALID_USERNAME);
        formData.add(PASSWORD_KEY, VALID_PASSWORD);

        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(formData);

        responseSpec.expectStatus().isBadRequest();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendLoginRequest(MultiValueMap<String, String> formData) {
        formData.add("flow_id", currentFlowId);
        return webTestClient.post()
                .uri("/login")
                .body(BodyInserters.fromFormData(formData))
                .cookie("SESSION", currentSessionId)
                .exchange();
    }
}
