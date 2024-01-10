package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.net.URISyntaxException;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
class AuthorizeEndpointTest {

    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ServerSecurityContextRepository securityContextRepository;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ValidRequestTest {

        // Maybe leads to fragile test ???
        @BeforeEach
        void setUp() {
            when(securityContextRepository.load(any()))
                    .thenReturn(Mono.just(
                            new SecurityContextImpl(UsernamePasswordAuthenticatedResourceOwnerAuthentication.builder()
                                    .principal("odeyalo")
                                    .credentials("password")
                                    .resourceOwner(ResourceOwner.builder()
                                            .principal("odeyalo")
                                            .availableScopes(ScopeContainer.singleScope(
                                                    SimpleScope.withName("write")
                                            )).build())

                                    .build())
                    ));
        }

        @Test
        void shouldReturnRedirectStatus() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectStatus().isFound();
        }

        @Test
        void shouldReturnRedirectToConsentPage() throws URISyntaxException {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(String.class).getResponseHeaders();

            URI uri = headers.getLocation();

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
        void shouldReturnRedirectToConsentPageAndContainFlowId() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(String.class).getResponseHeaders();

            URI uri = headers.getLocation();

            assertThat(uri).isNotNull();
            assertThat(uri).hasParameter("flow_id");
        }

        @Test
        void shouldReturn400BadRequestIfResponseTypeNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdNotIncluded() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfClientIdIsNotExist() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, "not_exist")
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, ALLOWED_REDIRECT_URI)
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @Test
        void shouldReturn400BadRequestIfRedirectUriIsNotRegistered() {
            WebTestClient.ResponseSpec exchange = webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/authorize")
                                    .queryParam(CLIENT_ID, EXISTING_CLIENT_ID)
                                    .queryParam(RESPONSE_TYPE, "token")
                                    .queryParam(REDIRECT_URI, "http:localhost:9812/redirect/not/allowed")
                                    .queryParam(SCOPE, "read write")
                                    .queryParam(STATE, "opaque")
                                    .build())
                    .exchange();

            exchange.expectStatus().isBadRequest();
        }

        @NotNull
        private WebTestClient.ResponseSpec sendValidAuthorizeRequest() {
            return webTestClient.get()
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
        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class UnauthorizedUserAuthorizeRequestTest {


        @BeforeEach
        void setup() {
            when(securityContextRepository.load(any()))
                    .thenReturn(Mono.empty());
        }

        @Test
        void shouldReturn302Status() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            responseSpec.expectStatus().isFound();
        }

        @Test
        void shouldRedirectToLoginPage() throws URISyntaxException {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();
            String location = headers.getFirst(HttpHeaders.LOCATION);

            assertThat(location).isNotNull();

            URI uri = URI.create(location);

            assertThat(
                    new URI(uri.getScheme(),
                            uri.getAuthority(),
                            uri.getPath(),
                            null, // Ignore the query part of the input url
                            uri.getFragment())
                            .toString()
            ).isEqualTo("/login");
        }

        @Test
        void redirectUriShouldContainFlowIdQueryParam() {
            WebTestClient.ResponseSpec responseSpec = sendValidAuthorizeRequest();

            HttpHeaders headers = responseSpec.returnResult(ResponseEntity.class).getResponseHeaders();
            String location = headers.getFirst(HttpHeaders.LOCATION);

            assertThat(location).isNotNull();

            URI uri = URI.create(location);

            assertThat(uri).hasParameter("flow_id");
        }

        @NotNull
        private WebTestClient.ResponseSpec sendValidAuthorizeRequest() {
            return webTestClient.get()
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
        }
    }
}