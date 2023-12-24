package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.*;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.token.access.MockOauth2AccessTokenGenerator;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Objects;

class ImplicitOauth2ResponseTypeHandlerTest {

    @Test
    void shouldReturnAuthorizationResponseOfSpecificTypeOnSuccess() {
        var authorizationRequest = createValidAuthorizationRequest();

        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId(authorizationRequest.getClientId()))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        InMemoryOauth2RegisteredClientService clientService = new InMemoryOauth2RegisteredClientService(client);

        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value"),
                clientService
        );

        testable.permissionGranted(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .as(StepVerifier::create)
                .expectNextMatches(response -> response instanceof ImplicitOauth2AuthorizationResponse)
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithAccessToken() {
        var authorizationRequest = createValidAuthorizationRequest();

        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId(authorizationRequest.getClientId()))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        InMemoryOauth2RegisteredClientService clientService = new InMemoryOauth2RegisteredClientService(client);

        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_access_token"),
                clientService
        );

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
        var authorizationRequest = createValidAuthorizationRequest();

        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId(authorizationRequest.getClientId()))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        InMemoryOauth2RegisteredClientService clientService = new InMemoryOauth2RegisteredClientService(client);

        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value"),
                clientService
        );

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
        var authorizationRequest = createValidAuthorizationRequest();

        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId(authorizationRequest.getClientId()))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        InMemoryOauth2RegisteredClientService clientService = new InMemoryOauth2RegisteredClientService(client);

        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value"),
                clientService
        );

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
        var authorizationRequest = createValidAuthorizationRequest();

        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId(authorizationRequest.getClientId()))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        InMemoryOauth2RegisteredClientService clientService = new InMemoryOauth2RegisteredClientService(client);

        ImplicitOauth2ResponseTypeHandler testable = new ImplicitOauth2ResponseTypeHandler(
                new MockOauth2AccessTokenGenerator("my_token_value"),
                clientService
        );

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