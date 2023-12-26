package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.client.ClientProfile;
import com.odeyalo.sonata.cello.core.client.ClientType;
import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.validation.ImplicitOauth2AuthorizationRequestValidationProvider;
import com.odeyalo.sonata.cello.exception.Oauth2AuthorizationRequestValidationException;
import com.odeyalo.sonata.cello.exception.UnacceptableOauth2RedirectUriException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ImplicitOauth2AuthorizationRequestValidationProviderTest {

    @Test
    void shouldReturnFalseIfNotImplicitRequest() {
        ImplicitOauth2AuthorizationRequestValidationProvider testable = new ImplicitOauth2AuthorizationRequestValidationProvider(
                new InMemoryOauth2RegisteredClientService()
        );

        Oauth2AuthorizationRequest mockRequest = new Oauth2AuthorizationRequest() {
        };

        testable.supports(mockRequest)
                .as(StepVerifier::create)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueIfImplicitRequest() {
        ImplicitOauth2AuthorizationRequestValidationProvider testable = new ImplicitOauth2AuthorizationRequestValidationProvider(
                new InMemoryOauth2RegisteredClientService()
        );

        ImplicitOauth2AuthorizationRequest request = ImplicitOauth2AuthorizationRequest.of(
                "123",
                RedirectUri.create("http://localhost:3000/callback"),
                ScopeContainer.singleScope(SimpleScope.withName("write")),
                "hello"
        );

        testable.supports(request)
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfClientIdNotExist() {
        ImplicitOauth2AuthorizationRequestValidationProvider testable = new ImplicitOauth2AuthorizationRequestValidationProvider(
                new InMemoryOauth2RegisteredClientService()
        );

        ImplicitOauth2AuthorizationRequest request = ImplicitOauth2AuthorizationRequest.of(
                "123",
                RedirectUri.create("http://localhost:3000/callback"),
                ScopeContainer.singleScope(SimpleScope.withName("write")),
                "hello"
        );
        testable.validate(request)
                .as(StepVerifier::create)
                .expectError(Oauth2AuthorizationRequestValidationException.class)
                .verify();
    }

    @Test
    void shouldReturnEmptyIfClientIdExist() {
        ImplicitOauth2AuthorizationRequestValidationProvider testable = new ImplicitOauth2AuthorizationRequestValidationProvider(
                new InMemoryOauth2RegisteredClientService(

                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyalo"))
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .clientType(ClientType.PUBLIC)
                                .allowedRedirectUris(RedirectUris.single(
                                        RedirectUri.create("http://localhost:3000/callback")
                                ))
                                .build()
                )
        );

        ImplicitOauth2AuthorizationRequest request = ImplicitOauth2AuthorizationRequest.of(
                "odeyalo",
                RedirectUri.create("http://localhost:3000/callback"),
                ScopeContainer.singleScope(SimpleScope.withName("write")),
                "hello"
        );

        testable.validate(request)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfRedirectUriIsNotAllowed() {
        ImplicitOauth2AuthorizationRequestValidationProvider testable = new ImplicitOauth2AuthorizationRequestValidationProvider(
                new InMemoryOauth2RegisteredClientService(

                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyalo"))
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .clientType(ClientType.PUBLIC)
                                .build()
                )
        );

        ImplicitOauth2AuthorizationRequest request = ImplicitOauth2AuthorizationRequest.of(
                "odeyalo",
                RedirectUri.create("http://localhost:3000/callback"),
                ScopeContainer.singleScope(SimpleScope.withName("write")),
                "hello"
        );

        testable.validate(request)
                .as(StepVerifier::create)
                .expectError(UnacceptableOauth2RedirectUriException.class)
                .verify();
    }
}