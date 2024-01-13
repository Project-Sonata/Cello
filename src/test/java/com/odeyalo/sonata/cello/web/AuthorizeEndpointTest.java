package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.AutoconfigureCelloWebTestClient;
import testing.CelloWebTestClient;
import testing.ImplicitSpecs;
import testing.WithAuthenticatedResourceOwner;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static testing.UriAssert.assertThat;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoconfigureCelloWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
class AuthorizeEndpointTest {
    // Bean is injected using AutoconfigureCelloWebTestClient
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    @WithAuthenticatedResourceOwner
    class ValidRequestTest {

        @Test
        void shouldReturnRedirectStatus() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectStatus().isFound();
        }

        @Test
        void shouldReturnRedirectToConsentPage() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(String.class).getResponseHeaders();

            URI uri = headers.getLocation();

            assertThat(uri).isNotNull();
            assertThat(uri).isEqualToWithoutQueryParameters("/oauth2/consent");
        }

        @Test
        void shouldReturnRedirectToConsentPageAndContainFlowId() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(String.class).getResponseHeaders();

            URI uri = headers.getLocation();

            assertThat(uri).isNotNull();
            assertThat(uri).hasParameter("flow_id");
        }
    }


    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class InvalidAuthorizationRequestTest {

        @Test
        void shouldReturn400BadRequestIfResponseTypeNotIncluded() {
            CelloWebTestClient.ImplicitSpec validImplicitSpec = ImplicitSpecs.valid();

            WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendUnknownAuthorizationRequest(validImplicitSpec);

            responseSpec.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdNotIncluded() {
            CelloWebTestClient.ImplicitSpec withoutClientId = ImplicitSpecs.withoutClientId();

            WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(withoutClientId);

            responseSpec.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdIsNotExist() {
            CelloWebTestClient.ImplicitSpec withNotExistingClientId = ImplicitSpecs.withInvalidClientId();

            WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(withNotExistingClientId);

            responseSpec.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfRedirectUriIsNotRegistered() {
            CelloWebTestClient.ImplicitSpec withUnallowedRedirectUri = ImplicitSpecs.withUnallowedRedirectUri();

            WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(withUnallowedRedirectUri);

            responseSpec.expectStatus().isBadRequest();
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class UnauthorizedUserAuthorizeRequestTest {

        @Test
        void shouldReturn302Status() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectStatus().isFound();
        }

        @Test
        void shouldRedirectToLoginPage() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();

            String location = headers.getFirst(HttpHeaders.LOCATION);

            //noinspection DataFlowIssue
            URI uri = URI.create(location);

            assertThat(uri).isEqualToWithoutQueryParameters("/oauth2/login");
        }

        @Test
        void redirectUriShouldContainFlowIdQueryParam() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();
            String location = headers.getFirst(HttpHeaders.LOCATION);

            //noinspection DataFlowIssue
            URI uri = URI.create(location);

            assertThat(uri).hasParameter("flow_id");
        }
    }

    @NotNull
    private WebTestClient.ResponseSpec sendValidAuthorizeRequest() {
        CelloWebTestClient.ImplicitSpec validImplicitSpec = ImplicitSpecs.valid();

        return celloWebTestClient.implicit().sendRequest(validImplicitSpec);
    }
}