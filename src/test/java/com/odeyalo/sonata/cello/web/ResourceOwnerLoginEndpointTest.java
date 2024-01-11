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
import testing.*;
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
@AutoconfigureCelloWebTestClient
public class ResourceOwnerLoginEndpointTest {
    private static final String VALID_USERNAME = "odeyalo";
    private static final String VALID_PASSWORD = "password";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAMETER_NAME = "flow_id";

    @Autowired
    WebTestClient webTestClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    @MockBean
    ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager;

    String currentFlowId;
    String currentSessionId;

    @BeforeEach
    void prepare() {
        WebTestClient.ResponseSpec exchange = celloWebTestClient.implicit().sendRequest(ImplicitSpecs.valid());

        exchange.expectStatus().isFound();

        FluxExchangeResult<Void> result = exchange.returnResult(Void.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need this values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();

        currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAMETER_NAME);
        currentSessionId = sessionCookie.getValue();
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
    void shouldReturnRedirectToConsentPage() {

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

        UriAssert.assertThat(uri).isEqualToWithoutQueryParameters("/oauth2/consent");
    }

    @Test
    void shouldReturnRedirectToConsentPageWithFlowIdQueryParameter() {

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
