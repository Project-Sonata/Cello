package com.odeyalo.sonata.cello.web;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import testing.AutoconfigureCelloWebTestClient;
import testing.CelloWebTestClient;
import testing.ImplicitSpecs;
import testing.UriUtils;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@AutoconfigureCelloWebTestClient
public final class ThirdPartyOauth2CallbackEndpointTest {

    @Autowired
    WebTestClient webTestClient;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAM_NAME = "flow_id";

    static final String PROVIDER_NAME = "provider";

    @Test
    void shouldRedirectToConsentPageOnSuccessCallback() {
        WebTestClient.ResponseSpec responseSpec = sendValidRequest();

        responseSpec.expectStatus().isFound();
    }

    @NotNull
    private WebTestClient.ResponseSpec sendValidRequest() {
        Pair<String, String> flowSessionPair = prepareAuthorizationFlow();

        return webTestClient.get()
                .uri(builder -> builder.path("/oauth2/login/" + PROVIDER_NAME + "/callback")
                        .queryParam(FLOW_ID_QUERY_PARAM_NAME, flowSessionPair.getFirst())
                        .build())
                .cookie(SESSION_COOKIE_NAME, flowSessionPair.getSecond())
                .exchange();
    }

    private Pair<String, String> prepareAuthorizationFlow() {
        WebTestClient.ResponseSpec responseSpec = celloWebTestClient.implicit().sendRequest(ImplicitSpecs.valid());

        responseSpec.expectStatus().isFound();

        FluxExchangeResult<Void> result = responseSpec.returnResult(Void.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionCookie = result.getResponseCookies().getFirst(SESSION_COOKIE_NAME);

        // we need this values to send valid requests, otherwise HTTP 400 BAD Request will be returned :(
        assertThat(uri).isNotNull();
        assertThat(sessionCookie).isNotNull();


        String currentFlowId = UriUtils.parseQueryParameters(uri).get(FLOW_ID_QUERY_PARAM_NAME);
        String currentSessionId = sessionCookie.getValue();

        return new Pair<>(currentFlowId, currentSessionId);
    }

}
