package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderAuthorizationCodeAuthenticationCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultOauth2ProviderAuthenticationCredentialsConverterTest {

    @Test
    void shouldReturnCredentialsOfCorrectType() {
        var testable = new DefaultOauth2ProviderAuthenticationCredentialsConverter(
                StaticCallbackOauth2ProviderNameResolver.alwaysReturn("google")
        );

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/google/callback?code=miku123&state=testState").build()
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(it -> it instanceof Oauth2ProviderAuthorizationCodeAuthenticationCredentials)
                .verifyComplete();
    }

    @Test
    void shouldReturnCorrectOauth2ProviderNameThatPresentInRequest() {
        var testable = new DefaultOauth2ProviderAuthenticationCredentialsConverter(
                StaticCallbackOauth2ProviderNameResolver.alwaysReturn("google")
        );

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/google/callback?code=miku123&state=testState").build()
        );

        testable.convertToCredentials(exchange)
                .cast(Oauth2ProviderAuthorizationCodeAuthenticationCredentials.class)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getProviderName()).isEqualTo("google"))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeEqualToOneInRequestParametersBasedOnOauth2Spec() {
        var testable = new DefaultOauth2ProviderAuthenticationCredentialsConverter(
                StaticCallbackOauth2ProviderNameResolver.alwaysReturn("google")
        );

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/google/callback?code=miku123&state=testState").build()
        );

        testable.convertToCredentials(exchange)
                .cast(Oauth2ProviderAuthorizationCodeAuthenticationCredentials.class)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getAuthorizationCode()).isEqualTo("miku123"))
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfCodeParameterDoesNotPresent() {
        var testable = new DefaultOauth2ProviderAuthenticationCredentialsConverter(
                StaticCallbackOauth2ProviderNameResolver.alwaysReturn("google")
        );

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/google/callback?state=testState").build()
        );

        testable.convertToCredentials(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}