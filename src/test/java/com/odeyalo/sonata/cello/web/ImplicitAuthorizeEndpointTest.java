package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Integration tests for implicit response type only
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
public class ImplicitAuthorizeEndpointTest {
    public static final String STATE_PARAMETER_VALUE = "opaque";
    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";
    @Autowired
    WebTestClient webTestClient;

    final String AUTHENTICATION_COOKIE_NAME = "clid";
    final String AUTHENTICATION_COOKIE_VALUE = "odeyalo";

    @MockBean
    Oauth2AuthorizationRequestRepository oauth2AuthorizationRequestRepository;

    @BeforeEach
    void setUp() {
        when(oauth2AuthorizationRequestRepository.loadAuthorizationRequest(any()))
                .thenReturn(Mono.just(
                        ImplicitOauth2AuthorizationRequest.builder()
                                .clientId(EXISTING_CLIENT_ID)
                                .redirectUri(RedirectUri.create(ALLOWED_REDIRECT_URI))
                                .scopes(ScopeContainer.singleScope(SimpleScope.withName("read")))
                                .state(STATE_PARAMETER_VALUE)
                                .build()
                ));

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
        return webTestClient.post()
                .uri(builder ->
                        builder
                                .path("/oauth2/consent")
                                .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("action", "approved"))
                .cookie(AUTHENTICATION_COOKIE_NAME, AUTHENTICATION_COOKIE_VALUE)
                .exchange();
    }
}
