package com.odeyalo.sonata.cello.web;


import com.odeyalo.sonata.cello.core.DefaultOauth2ResponseTypes;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.UriAssert;
import testing.WithAuthenticatedResourceOwner;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@WithAuthenticatedResourceOwner
@AutoConfigureWebTestClient
public final class AuthorizationCodeAuthorizeEndpointTest {
    static final String STATE_PARAMETER_VALUE = "opaque";
    static final String EXISTING_CLIENT_ID = "123";
    static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;


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
