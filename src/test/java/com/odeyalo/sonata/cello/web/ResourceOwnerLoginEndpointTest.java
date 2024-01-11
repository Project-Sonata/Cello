package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.InMemoryResourceOwnerService;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticationManager;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordResourceOwnerAuthenticationManager;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@AutoconfigureCelloWebTestClient
@Import(ResourceOwnerLoginEndpointTest.Config.class)
public class ResourceOwnerLoginEndpointTest {
    private static final String VALID_USERNAME = "odeyalo";
    private static final String VALID_PASSWORD = "password";

    private static final String INVALID_USERNAME = "invalid";
    private static final String INVALID_PASSWORD = "invalid";

    private static final String SESSION_COOKIE_NAME = "SESSION";
    private static final String FLOW_ID_QUERY_PARAMETER_NAME = "flow_id";

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    String currentFlowId;
    String currentSessionId;

    @BeforeEach
    void prepare() {
        WebTestClient.ResponseSpec exchange = celloWebTestClient.implicit().sendRequest(ImplicitSpecs.valid());

        exchange.expectStatus().isFound();

        FluxExchangeResult<Void> result = exchange.returnResult(Void.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need these values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();

        currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAMETER_NAME);
        currentSessionId = sessionCookie.getValue();
    }

    @Test
    void shouldReturnRedirectIfLoginSuccess() {
        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(VALID_USERNAME, VALID_PASSWORD);

        responseSpec.expectStatus().isFound();
    }

    @Test
    void shouldReturnRedirectToConsentPage() {
        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(VALID_USERNAME, VALID_PASSWORD);

        HttpHeaders responseHeaders = responseSpec.returnResult(String.class).getResponseHeaders();

        URI uri = responseHeaders.getLocation();

        UriAssert.assertThat(uri).isEqualToWithoutQueryParameters("/oauth2/consent");
    }

    @Test
    void shouldReturnRedirectToConsentPageWithFlowIdQueryParameter() {
        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(VALID_USERNAME, VALID_PASSWORD);

        HttpHeaders responseHeaders = responseSpec.returnResult(String.class).getResponseHeaders();

        URI uri = responseHeaders.getLocation();

        UriAssert.assertThat(uri).hasNotEmptyQueryParameter("flow_id");
    }

    @Test
    void shouldReturnBadRequestStatusIfCredentialsInvalid() {
        WebTestClient.ResponseSpec responseSpec = sendLoginRequest(INVALID_USERNAME, INVALID_PASSWORD);

        responseSpec.expectStatus().isBadRequest();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendLoginRequest(String username, String password) {
        return celloWebTestClient.login()
                .sessionId(currentSessionId)
                .flowId(currentFlowId)
                .ready()
                .usernamePasswordLogin(username, password);
    }

    @TestConfiguration
    public static class Config {

        @Bean
        @Primary
        public ResourceOwnerAuthenticationManager testingResourceOwnerAuthenticationManager() {
            ResourceOwner existingUser = ResourceOwner.builder()
                    .principal(VALID_USERNAME)
                    .credentials(VALID_PASSWORD)
                    .build();

            return new UsernamePasswordResourceOwnerAuthenticationManager(
                    new InMemoryResourceOwnerService(
                            Collections.singletonList(existingUser)
                    )
            );
        }
    }
}
