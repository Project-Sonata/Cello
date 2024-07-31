package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.consent.ApprovedConsentDecision;
import com.odeyalo.sonata.cello.web.dto.Oauth2AccessTokenResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@WithAuthenticatedResourceOwner
@AutoConfigureWebTestClient
@AutoconfigureCelloWebTestClient
public class AuthorizationCodeExchangeEndpointTest {

    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAMETER_NAME = "flow_id";

    String currentFlowId;
    String currentSessionId;

    @Autowired
    WebTestClient webTestClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;


    @BeforeEach
    void prepare() {
        prepareAuthorizationFlow();
    }


    @Test
    void shouldReturn200OkStatusIfRequestIsValid() {
        final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec = AuthorizationCodeSpecs.valid();

        final String authorizationCode = sendInitialAuthorizationCodeFlowRequest(authorizationCodeSpec);

        final WebTestClient.ResponseSpec responseSpec = sendCodeExchangeRequest(authorizationCodeSpec, authorizationCode);

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnResponseInApplicationJson() {
        final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec = AuthorizationCodeSpecs.valid();

        final String authorizationCode = sendInitialAuthorizationCodeFlowRequest(authorizationCodeSpec);

        final WebTestClient.ResponseSpec responseSpec = sendCodeExchangeRequest(authorizationCodeSpec, authorizationCode);

        responseSpec.expectHeader().contentType(APPLICATION_JSON);
    }

    @Test
    void shouldReturnAccessTokenInResponseForValidAuthorizationCode() {
        final Oauth2AccessTokenResponse accessTokenResponse = requestAccessTokenExchange();

        assertThat(accessTokenResponse.getAccessToken()).isNotNull();
    }

    @Test
    void shouldReturnAccessTokenExpirationTimeInResponse() {
        final Oauth2AccessTokenResponse accessTokenResponse = requestAccessTokenExchange();

        assertThat(accessTokenResponse.getExpiresIn()).isNotNull();
    }

    @Test
    void shouldReturnAccessTokenTypeInResponse() {
        final Oauth2AccessTokenResponse accessTokenResponse = requestAccessTokenExchange();

        assertThat(accessTokenResponse.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldReturnErrorIfAuthorizationCodeDoesNotExist() {
        final WebTestClient.ResponseSpec accessTokenResponse = requestAccessTokenExchangeUsingInvalidCode();

        accessTokenResponse.expectStatus().isBadRequest();
    }

    @NotNull
    private Oauth2AccessTokenResponse requestAccessTokenExchange() {
        final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec = AuthorizationCodeSpecs.valid();

        final String authorizationCode = sendInitialAuthorizationCodeFlowRequest(authorizationCodeSpec);

        final WebTestClient.ResponseSpec responseSpec = sendCodeExchangeRequest(authorizationCodeSpec, authorizationCode);

        return Objects.requireNonNull(
                responseSpec.expectBody(Oauth2AccessTokenResponse.class).returnResult().getResponseBody(),
                "Missing response body for token exchange request"
        );
    }

    private WebTestClient.@NotNull ResponseSpec requestAccessTokenExchangeUsingInvalidCode() {
        final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec = AuthorizationCodeSpecs.valid();

        sendInitialAuthorizationCodeFlowRequest(authorizationCodeSpec);

        return sendCodeExchangeRequest(authorizationCodeSpec, "not_existing_code");
    }

    @NotNull
    private String sendInitialAuthorizationCodeFlowRequest(final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec) {
        final WebTestClient.ResponseSpec awaitingConsentResponse = celloWebTestClient.authorizationCode().sendRequest(authorizationCodeSpec);

        final WebTestClient.ResponseSpec accessTokenResponse = sendApproveConsentRequest();

        final URI redirectUri = accessTokenResponse.returnResult(Void.class).getResponseHeaders().getLocation();

        assertThat(redirectUri).isNotNull();

        final MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(redirectUri)
                .build().getQueryParams();

        return Objects.requireNonNull(
                queryParams.getFirst("code"),
                "An authorization code is required to test authorization code exchange flow"
        );
    }

    @NotNull
    private WebTestClient.ResponseSpec sendCodeExchangeRequest(final CelloWebTestClient.AuthorizationCodeSpec authorizationCodeSpec, final String authorizationCode) {
        return webTestClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(
                        "grant_type", "authorization_code")
                        .with("code", authorizationCode)
                        .with("redirect_uri", authorizationCodeSpec.getRedirectUri())
                        .with("client_id", authorizationCodeSpec.getClientId())
                )
                .exchange();
    }


    private void prepareAuthorizationFlow() {
        final CelloWebTestClient.AuthorizationCodeSpec initialAuthorizationCodeRequest = AuthorizationCodeSpecs.valid();
        final WebTestClient.ResponseSpec responseSpec = celloWebTestClient.authorizationCode().sendRequest(initialAuthorizationCodeRequest);

        responseSpec.expectStatus().isFound();

        final FluxExchangeResult<Void> result = responseSpec.returnResult(Void.class);

        final URI uri = result.getResponseHeaders().getLocation();
        final ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need this values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();

        currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAMETER_NAME);
        currentSessionId = sessionCookie.getValue();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendApproveConsentRequest() {

        return celloWebTestClient.consentPage()
                .authenticatedUser()
                .withSessionId(currentSessionId)
                .and()
                .withFlowId(currentFlowId)
                .ready()
                .submitConsentDecision(new ApprovedConsentDecision());
    }
}
