package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import testing.*;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@RegisterOauth2Clients
@AutoconfigureCelloWebTestClient
@WithAuthenticatedResourceOwner
@Import(ConsentPageEndpointTest.Config.class)
public class ConsentPageEndpointTest {

    // Created in AutoconfigureCelloWebTestClient config
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    CelloWebTestClient celloWebTestClient;

    static final String HTML_CONTENT = "<h1>Hello Odeyalo!</h1>";

    static final String SESSION_COOKIE_NAME = "SESSION";
    static final String FLOW_ID_QUERY_PARAM_NAME = "flow_id";
    // Bad practice, can't run this tests in concurrency mode, tests will be unpredictable because of race conditions,
    // I don't find any solution yet :(
    String currentFlowId;
    String currentSessionId;

    @BeforeEach
    void prepare() {
        prepareAuthorizationFlow();
    }

    @Test
    void shouldReturn200OKStatus() {
        WebTestClient.ResponseSpec responseSpec = sendGetConsentPageRequest();

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnHtmlPage() {
        WebTestClient.ResponseSpec responseSpec = sendGetConsentPageRequest();

        responseSpec.expectBody(String.class).isEqualTo(HTML_CONTENT);
    }

    @TestConfiguration
    static class Config {
        @Bean
        public Oauth2ConsentPageProvider oauth2ConsentPageProvider() {
            return (request, resourceOwner, exchange) -> {
                ServerHttpResponse response = exchange.getResponse();
                response.getHeaders().setContentType(MediaType.TEXT_HTML);
                return response.writeWith(
                        Flux.just(response.bufferFactory().wrap(HTML_CONTENT.getBytes()))
                );
            };
        }
    }

    private WebTestClient.ResponseSpec sendGetConsentPageRequest() {
        return celloWebTestClient.consentPage()
                .withFlowId(currentFlowId)
                .authenticatedUser()
                .withSessionId(currentSessionId)
                .and()
                .ready()
                .getConsentPage();
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
}