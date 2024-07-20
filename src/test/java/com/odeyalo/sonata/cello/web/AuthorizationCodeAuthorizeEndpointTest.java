package com.odeyalo.sonata.cello.web;


import com.odeyalo.sonata.cello.core.DefaultOauth2ResponseTypes;
import com.odeyalo.sonata.cello.core.consent.ApprovedConsentDecision;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@WithAuthenticatedResourceOwner
@AutoConfigureWebTestClient
@AutoconfigureCelloWebTestClient
public final class AuthorizationCodeAuthorizeEndpointTest {
    static final String STATE_PARAMETER_VALUE = "opaque";
    static final String EXISTING_CLIENT_ID = "123";
    static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InitialAuthorizationCodeFlowRequestTest {

        @Test
        void shouldSendRedirectToConsentPage() {

            final WebTestClient.ResponseSpec responseSpec = sendValidInitialAuthorizationCodeRequest();

            responseSpec.expectStatus().isFound();

            final HttpHeaders headers = responseSpec.returnResult(Void.class).getResponseHeaders();

            final URI locationUri = headers.getLocation();

            UriAssert.assertThat(locationUri).isEqualToWithoutQueryParameters("/oauth2/consent");
        }

        @Test
        void shouldReturnBadRequestStatusIfClientIdIsMissing() {
            final WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                    .uri(builder -> builder.path("/oauth2/authorize")
                            .queryParam(RESPONSE_TYPE, DefaultOauth2ResponseTypes.AUTHORIZATION_CODE.getName())
                            .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                            .queryParam(SCOPE, "read write")
                            .queryParam(STATE, STATE_PARAMETER_VALUE)
                            .build())
                    .exchange();

            responseSpec.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturnBadRequestStatusIfRedirectUriIsMissing() {
            final WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                    .uri(builder -> builder.path("/oauth2/authorize")
                            .queryParam(RESPONSE_TYPE, DefaultOauth2ResponseTypes.AUTHORIZATION_CODE.getName())
                            .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                            .queryParam(SCOPE, "read write")
                            .queryParam(STATE, STATE_PARAMETER_VALUE)
                            .build())
                    .exchange();

            responseSpec.expectStatus().isBadRequest();
        }

        @NotNull
        private WebTestClient.ResponseSpec sendValidInitialAuthorizationCodeRequest() {
            return webTestClient.get()
                    .uri(builder -> builder.path("/oauth2/authorize")
                            .queryParam(RESPONSE_TYPE, DefaultOauth2ResponseTypes.AUTHORIZATION_CODE.getName())
                            .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                            .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                            .queryParam(SCOPE, "read write")
                            .queryParam(STATE, STATE_PARAMETER_VALUE)
                            .build())
                    .exchange();
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class AfterConsentApprovedTest {
        static final String SESSION_COOKIE_NAME = "SESSION";
        static final String FLOW_ID_QUERY_PARAMETER_NAME = "flow_id";

        String currentFlowId;
        String currentSessionId;

        @BeforeEach
        void prepare() {
            prepareAuthorizationFlow();
        }

        @Test
        void shouldReturnRedirectToUriThatWasProvidedInInitialRequest() {
            final WebTestClient.ResponseSpec responseSpec = sendApproveConsentRequest();

            responseSpec.expectStatus().isFound();

            final HttpHeaders headers = responseSpec.returnResult(Void.class).getResponseHeaders();

            UriAssert.assertThat(headers.getLocation()).isEqualToWithoutQueryParameters(AuthorizationCodeSpecs.ALLOWED_REDIRECT_URI);
        }

        @Test
        void shouldContainStateParameterInRedirectUriAsWasProvidedInInitialRequest() {
            final WebTestClient.ResponseSpec responseSpec = sendApproveConsentRequest();

            final HttpHeaders headers = responseSpec.returnResult(Void.class).getResponseHeaders();

            UriAssert.assertThat(headers.getLocation()).hasParameter("state", AuthorizationCodeSpecs.STATE_VALUE);
        }

        @Test
        void shouldReturnAuthorizationCodeInUriQueryParameters() {
            final WebTestClient.ResponseSpec responseSpec = sendApproveConsentRequest();

            final HttpHeaders headers = responseSpec.returnResult(Void.class).getResponseHeaders();

            UriAssert.assertThat(headers.getLocation()).hasNotEmptyQueryParameter("code");
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
}
