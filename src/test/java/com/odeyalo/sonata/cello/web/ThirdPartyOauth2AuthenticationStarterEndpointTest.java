package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.oauth2.InMemoryOauth2ProviderRegistrationRepository;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistrationRepository;
import com.odeyalo.sonata.cello.web.ThirdPartyOauth2AuthenticationStarterEndpointTest.Config;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@AutoconfigureCelloWebTestClient
@Import(Config.class)
public final class ThirdPartyOauth2AuthenticationStarterEndpointTest {

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
    void shouldReturnRedirectWithProvidedClientId() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).hasParameter("client_id", "odeyalooo");
    }

    @Test
    void shouldReturnRedirectWithProvidedRedirectUriQueryParam() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).hasParameter("redirect_uri", "http://localhost:3000");
    }

    @Test
    void shouldReturnRedirectWithProvidedScopesQueryParam() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).hasParameter("scope", "read write");
    }

    @Test
    void shouldReturnRedirectWithGeneratedState() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).hasParameter("state");
    }

    @Test
    void shouldUseAuthorizationCodeGrantType() {
        WebTestClient.ResponseSpec responseSpec = sendGoogleAuthenticationRequest();

        URI redirectLocation = responseSpec.returnResult(ResponseEntity.class)
                .getResponseHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).hasParameter("response_type", "code");
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

    @TestConfiguration
    public static class Config {

        @Bean
        @Primary
        public Oauth2ProviderRegistrationRepository testingOauth2ProviderRegistration() {
            Map<String, Oauth2ProviderRegistration> cache = Map.of("google", Oauth2ProviderRegistration.builder()
                    .providerUri("https://accounts.google.com/o/oauth2/v2/auth")
                    .clientId("odeyalooo")
                    .clientSecret("secret")
                    .redirectUri("http://localhost:3000")
                    .scopes(ScopeContainer.fromArray(
                            SimpleScope.withName("read"),
                            SimpleScope.withName("write")
                    ))
                    .build()
            );
            return new InMemoryOauth2ProviderRegistrationRepository(cache);
        }
    }
}
