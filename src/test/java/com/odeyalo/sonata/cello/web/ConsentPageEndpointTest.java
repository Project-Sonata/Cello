package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.UsernamePasswordAuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import testing.spring.configuration.RegisterOauth2Clients;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@RegisterOauth2Clients
@Import(ConsentPageEndpointTest.Config.class)
public class ConsentPageEndpointTest {
    @Autowired
    WebTestClient webTestClient;

    public static final String EXISTING_CLIENT_ID = "123";

    final String AUTHENTICATION_COOKIE_KEY = "clid";
    final String AUTHENTICATED_USER_COOKIE_VALUE = "odeyalo";
    public static final String  HTML_CONTENT = "<h1>Hello Odeyalo!</h1>";

    @MockBean
    Oauth2AuthorizationRequestRepository authorizationRequestRepository;
    @MockBean
    ServerSecurityContextRepository securityContextRepository;


    // Maybe leads to fragile test ???
    @BeforeEach
    void prepareSecurityContextRepository() {
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

    @BeforeEach
    void prepareAuthorizationRequest() {
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
    void shouldReturn200OKStatus() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/oauth2/consent")
                .cookie(AUTHENTICATION_COOKIE_KEY, AUTHENTICATED_USER_COOKIE_VALUE)
                .exchange();

        responseSpec.expectStatus().isOk();
    }

    @Test
    void shouldReturnHtmlPage() {
        WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                .uri("/oauth2/consent")
                .cookie(AUTHENTICATION_COOKIE_KEY, AUTHENTICATED_USER_COOKIE_VALUE)
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
}
