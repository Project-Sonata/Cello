package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
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
import testing.UriUtils;
import testing.WithAuthenticatedResourceOwner;
import testing.spring.configuration.RegisterOauth2Clients;

import java.net.URI;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
@WithAuthenticatedResourceOwner
@Import(ConsentPageEndpointTest.Config.class)
public class ConsentPageEndpointTest {
    @Autowired
    WebTestClient webTestClient;

    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    public static final String HTML_CONTENT = "<h1>Hello Odeyalo!</h1>";

    String currentFlowId;
    String currentSessionId;

    @BeforeEach
    void prepare() {
        prepareAuthorizationFlow();
    }

    @Test
    void shouldReturn200OKStatus() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(builder -> builder.path("/oauth2/consent").queryParam("flow_id", currentFlowId).build())
                .cookie("SESSION", currentSessionId)
                .exchange();

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnHtmlPage() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri(builder -> builder.path("/oauth2/consent").queryParam("flow_id", currentFlowId).build())
                .cookie("SESSION", currentSessionId)
                .exchange();

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


    private void prepareAuthorizationFlow() {
        WebTestClient.ResponseSpec exchange = webTestClient.get()
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

        exchange.expectStatus().isFound();

        FluxExchangeResult<String> result = exchange
                .returnResult(String.class);

        URI uri = result.getResponseHeaders().getLocation();
        ResponseCookie sessionId = result.getResponseCookies().getFirst("SESSION");

        currentFlowId = UriUtils.parseQueryParameters(uri).get("flow_id");
        currentSessionId = sessionId.getValue();
    }
}
