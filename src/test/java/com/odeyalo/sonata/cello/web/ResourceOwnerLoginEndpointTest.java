package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationManager;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

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
    public static final String REDIRECT_URI = "http://localhost:4000";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager;
    @MockBean
    ServerRequestCache serverRequestCache;
    @MockBean
    Oauth2AuthorizationRequestRepository authorizationRequestRepository;

    @BeforeEach
    void setUp() {
        when(serverRequestCache.getRedirectUri(any()))
                .thenReturn(Mono.just(
                        URI.create(REDIRECT_URI)
                ));

        when(authorizationRequestRepository.loadAuthorizationRequest(any()))
                .thenReturn(Mono.just(
                        ImplicitOauth2AuthorizationRequest.builder()
                                .clientId(EXISTING_CLIENT_ID)
                                .scopes(ScopeContainer.empty())
                                .redirectUri(RedirectUri.create(REDIRECT_URI))
                                .state("hello")
                                .build()
                ));
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

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/login")
                .body(BodyInserters.fromFormData(formData))
                .exchange();

        responseSpec.expectStatus().isFound();
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

        WebTestClient.ResponseSpec responseSpec = webTestClient.post()
                .uri("/login")
                .body(BodyInserters.fromFormData(formData))
                .exchange();

        responseSpec.expectStatus().isBadRequest();
    }
}
