package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.consent.ApprovedConsentDecision;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for implicit response type only
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@WithAuthenticatedResourceOwner
@AutoconfigureCelloWebTestClient
public class ImplicitAuthorizeEndpointTest {
    static final String STATE_PARAMETER_VALUE = "opaque";
    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAMETER_NAME = "flow_id";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

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

        return celloWebTestClient.consentPage()
                .authenticatedUser()
                .withSessionId(currentSessionId)
                .and()
                .withFlowId(currentFlowId)
                .ready()
                .submitConsentDecision(new ApprovedConsentDecision());
    }

    private void prepareAuthorizationFlow() {
        CelloWebTestClient.ImplicitSpec validImplicitRequest = ImplicitSpecs.valid();
        WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(validImplicitRequest);

        responseSpec.expectStatus().isFound();

        FluxExchangeResult<Void> result = responseSpec.returnResult(Void.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need this values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();

        currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAMETER_NAME);
        currentSessionId = sessionCookie.getValue();
    }
}
