package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.*;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;
import testing.UriAssert;
import testing.UriUtils;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ImplicitOauth2AuthorizationResponseConverter}
 *
 * @author odeyalooo
 */
class ImplicitOauth2AuthorizationResponseConverterTest {

    @Test
    void shouldReturnEmptyMonoIfItIsNotImplicit() {

        MockOauth2AuthorizationRequest mockAuthorizationRequest = MockOauth2AuthorizationRequest.create();

        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());

        testable.convert(MockOauth2AuthorizationResponse.create(mockAuthorizationRequest), httpExchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnRedirectToSameUriAsProvidedInAuthorizationRequest() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponse(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        assertThat(httpExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);

        URI location = httpExchange.getResponse().getHeaders().getLocation();

        UriAssert.assertThat(location).isEqualToWithoutQueryParameters("http://localhost:3000");
    }

    @Test
    void shouldReturnRedirectUriWithAccessTokenQueryParam() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponse(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        URI location = httpExchange.getResponse().getHeaders().getLocation();

        assertThat(location).hasParameter("access_token", implicitResponse.getAccessToken());
    }

    @Test
    void shouldReturnRedirectUriWithAccessTokenExpiresInQueryParam() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponse(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        URI location = httpExchange.getResponse().getHeaders().getLocation();

        assertThat(location).hasParameter("expires_in", String.valueOf(implicitResponse.getExpiresIn()));
    }

    @Test
    void shouldReturnRedirectUriWithAccessTokenTypeInQueryParam() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponse(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        URI location = httpExchange.getResponse().getHeaders().getLocation();

        assertThat(location).hasParameter("token_type", implicitResponse.getTokenType());
    }

    @Test
    void shouldReturnRedirectUriWithStateInQueryParam() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponse(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        URI location = httpExchange.getResponse().getHeaders().getLocation();

        assertThat(location).hasParameter("state", implicitResponse.getState());
    }

    // Implements https://github.com/Project-Sonata/Cello/issues/44
    @Test
    void shouldReturnRedirectWithScopesIfTheyAreDifferFromRequested() {
        ImplicitOauth2AuthorizationResponseConverter testable = new ImplicitOauth2AuthorizationResponseConverter();

        // given
        MockServerWebExchange httpExchange = MockServerWebExchange.from(MockServerHttpRequest.get("/authorize").build());
        ImplicitOauth2AuthorizationRequest implicitRequest = createImplicitRequest();
        ImplicitOauth2AuthorizationResponse implicitResponse = createImplicitResponseWithDifferentScopes(implicitRequest);

        // when
        testable.convert(implicitResponse, httpExchange)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // then
        URI location = httpExchange.getResponse().getHeaders().getLocation();

        assertThat(location).hasParameter("scope");

        ScopeContainer responseScopeContainer = parseScopes(location);

        assertThat(responseScopeContainer).containsAll(implicitResponse.getScope());

    }

    @NotNull
    private static ImplicitOauth2AuthorizationResponse createImplicitResponse(ImplicitOauth2AuthorizationRequest implicitRequest) {
        return ImplicitOauth2AuthorizationResponse.withAssociatedRequest(implicitRequest)
                .scope(implicitRequest.getScopes())
                .tokenType(Oauth2AccessToken.TokenType.BEARER.typeName())
                .expiresIn(3600L)
                .accessToken("access_token_value")
                .state(implicitRequest.getState())
                .build();
    }

    @NotNull
    private static ImplicitOauth2AuthorizationResponse createImplicitResponseWithDifferentScopes(ImplicitOauth2AuthorizationRequest implicitRequest) {
        return ImplicitOauth2AuthorizationResponse.withAssociatedRequest(implicitRequest)
                .scope(ScopeContainer.fromArray(SimpleScope.withName("read"), SimpleScope.withName("custom")))
                .tokenType(Oauth2AccessToken.TokenType.BEARER.typeName())
                .expiresIn(3600L)
                .accessToken("access_token_value")
                .state(implicitRequest.getState())
                .build();
    }

    @NotNull
    private static ImplicitOauth2AuthorizationRequest createImplicitRequest() {
        return ImplicitOauth2AuthorizationRequest.builder()
                .clientId("odeyalo")
                .scopes(ScopeContainer.singleScope(SimpleScope.withName("read")))
                .state("hello")
                .redirectUri(RedirectUri.create("http://localhost:3000"))
                .build();
    }

    @NotNull
    private static ScopeContainer parseScopes(URI location) {
        String scopes = UriUtils.parseQueryParameters(location).get("scope");

        List<SimpleScope> parsedScopes = Arrays.stream(scopes.split(" "))
                .map(SimpleScope::withName)
                .toList();

        return ScopeContainer.fromCollection(parsedScopes);
    }
}