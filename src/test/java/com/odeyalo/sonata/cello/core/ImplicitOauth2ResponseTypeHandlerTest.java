package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.token.access.*;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Objects;

class ImplicitOauth2ResponseTypeHandlerTest {

    @Test
    void shouldReturnAuthorizationResponseOfSpecificTypeOnSuccess() {
        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value")
        );
        var authorizationRequest = createValidAuthorizationRequest();

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> response instanceof ImplicitOauth2AuthorizationResponse)
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithAccessToken() {
        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_access_token")
        );

        var authorizationRequest = createValidAuthorizationRequest();

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> {
                    var implicitResponse = (ImplicitOauth2AuthorizationResponse) response;
                    return Objects.equals(implicitResponse.getAccessToken(), "my_access_token");
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithAccessTokenType() {
        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value")
        );

        var authorizationRequest = createValidAuthorizationRequest();

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> {
                    var implicitResponse = (ImplicitOauth2AuthorizationResponse) response;
                    return Objects.equals(implicitResponse.getTokenType(), Oauth2AccessToken.TokenType.BEARER.typeName());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithAccessTokenLifetime() {
        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value")
        );
        var authorizationRequest = createValidAuthorizationRequest();

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> {
                    var implicitResponse = (ImplicitOauth2AuthorizationResponse) response;
                    return implicitResponse.getExpiresIn() != null;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithStateEqualToProvided() {
        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value")
        );
        var authorizationRequest = createValidAuthorizationRequest();

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> {
                    var implicitResponse = (ImplicitOauth2AuthorizationResponse) response;
                    return Objects.equals(implicitResponse.getState(), authorizationRequest.getState());
                })
                .verifyComplete();
    }

    private static ImplicitOauth2AuthorizationRequest createValidAuthorizationRequest() {
        return ImplicitOauth2AuthorizationRequest.builder()
                .clientId("sonata-123")
                .redirectUri(new RedirectUri("https://localhost:8080"))
                .scopes(ScopeContainer.singleScope(SimpleScope.withName("write")))
                .state("123")
                .build();
    }
}