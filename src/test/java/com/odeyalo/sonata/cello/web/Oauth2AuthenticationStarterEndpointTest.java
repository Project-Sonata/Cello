package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@AutoconfigureCelloWebTestClient
public final class Oauth2AuthenticationStarterEndpointTest {

    @Autowired
    WebTestClient webTestClient;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAM_NAME = "flow_id";

    static final String GOOGLE_OAUTH2_URI_V2_VALUE = "https://accounts.google.com/o/oauth2/v2/auth";
    static final String GOOGLE_PROVIDER_NAME = "google";

    static final String NOT_SUPPORTED_PROVIDER_NAME = "not_supported";


    String currentFlowId;
    String currentSessionId;

    @BeforeAll
    void setup() {
        prepareAuthorizationFlow();
    }

    @Test
    void shouldReturnFoundStatusOnExistingProvider() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        responseSpec.expectStatus().isFound();

    }

    @Test
    void shouldReturnRedirectToProvidedRedirectUriForTheGivenProvider() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).isEqualToWithoutQueryParameters(GOOGLE_OAUTH2_URI_V2_VALUE);

    }

    @Test
    void shouldReturnBadRequestStatusOnNotSupportedProvider() {
        WebTestClient.ResponseSpec responseSpec = sendNotSupportedOauth2AuthenticationRequest();

        responseSpec.expectStatus().isBadRequest();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendGoogleAuthenticationRequest() {
        return sendThirdPartyOauth2AuthenticationRequest(GOOGLE_PROVIDER_NAME);
    }

    @NotNull
    private WebTestClient.ResponseSpec sendNotSupportedOauth2AuthenticationRequest() {
        return sendThirdPartyOauth2AuthenticationRequest(NOT_SUPPORTED_PROVIDER_NAME);
    }

    @NotNull
    private WebTestClient.ResponseSpec sendThirdPartyOauth2AuthenticationRequest(String providerName) {
        return webTestClient.get()
                .uri(builder -> builder
                        .path("/oauth2/login/" + providerName)
                        .queryParam(FLOW_ID_QUERY_PARAM_NAME, currentFlowId)
                        .build())
                .cookie(SESSION_COOKIE_NAME, currentSessionId)
                .exchange();
    }


    private void prepareAuthorizationFlow() {
        WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(ImplicitSpecs.valid());

        responseSpec.expectStatus().isFound();

        FluxExchangeResult<Void> result = responseSpec.returnResult(Void.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need this values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();

        currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAM_NAME);
        currentSessionId = sessionCookie.getValue();
    }
}
