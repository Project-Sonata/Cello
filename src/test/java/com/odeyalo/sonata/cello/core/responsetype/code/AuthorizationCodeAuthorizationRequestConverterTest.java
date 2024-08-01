package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.Scope;
import com.odeyalo.sonata.cello.exception.MalformedOauth2RequestException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import static com.odeyalo.sonata.cello.core.DefaultOauth2ResponseTypes.AUTHORIZATION_CODE;
import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationCodeAuthorizationRequestConverterTest {

    @Test
    void shouldReturnNotingIfHttpRequestIsNotAuthorizationCodeFlow() {
        final AuthorizationCodeAuthorizationRequestConverter testable = new AuthorizationCodeAuthorizationRequestConverter();

        final MockServerWebExchange webExchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:3000/oauth2/authorize")
                .queryParam(RESPONSE_TYPE, "token")
                .queryParam(CLIENT_ID, "miku")
                .queryParam(REDIRECT_URI, "http://localhost:3000/oauth2/callback")
                .queryParam(SCOPE, "read write")
                .queryParam(STATE, "state123")
                .build());

        testable.convert(webExchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnExceptionIfClientIdIsMissing() {
        final AuthorizationCodeAuthorizationRequestConverter testable = new AuthorizationCodeAuthorizationRequestConverter();

        final MockServerWebExchange webExchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:3000/oauth2/authorize")
                .queryParam(RESPONSE_TYPE, "code")
                .queryParam(REDIRECT_URI, "http://localhost:3000/oauth2/callback")
                .queryParam(SCOPE, "read write")
                .queryParam(STATE, "state123")
                .build());

        testable.convert(webExchange)
                .as(StepVerifier::create)
                .expectError(MalformedOauth2RequestException.class)
                .verify();
    }

    @Test
    void shouldReturnExceptionIfRedirectUrIsMissing() {
        final AuthorizationCodeAuthorizationRequestConverter testable = new AuthorizationCodeAuthorizationRequestConverter();

        final MockServerWebExchange webExchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:3000/oauth2/authorize")
                .queryParam(RESPONSE_TYPE, "code")
                .queryParam(CLIENT_ID, "miku123")
                .queryParam(SCOPE, "read write")
                .queryParam(STATE, "state123")
                .build());

        testable.convert(webExchange)
                .as(StepVerifier::create)
                .expectError(MalformedOauth2RequestException.class)
                .verify();
    }

    @Test
    void shouldIncludeRequiredOauth2Parameters() {
        final AuthorizationCodeAuthorizationRequestConverter testable = new AuthorizationCodeAuthorizationRequestConverter();

        final MockServerWebExchange webExchange = MockServerWebExchange.from(MockServerHttpRequest.get("http://localhost:3000/oauth2/authorize")
                .queryParam(RESPONSE_TYPE, "code")
                .queryParam(CLIENT_ID, "miku")
                .queryParam(REDIRECT_URI, "http://localhost:3000/oauth2/callback")
                .queryParam(SCOPE, "read write")
                .queryParam(STATE, "state123")
                .build());

        testable.convert(webExchange)
                .as(StepVerifier::create)
                .assertNext(res -> {
                    assertThat(res.getResponseType()).isEqualTo(AUTHORIZATION_CODE);
                    assertThat(res.getClientId()).isEqualTo("miku");
                    assertThat(res.getScopes()).map(Scope::getName).containsExactlyInAnyOrder("read", "write");
                    assertThat(res.getRedirectUri()).isEqualTo(RedirectUri.create("http://localhost:3000/oauth2/callback"));
                    assertThat(res.getState()).isEqualTo("state123");
                })
                .verifyComplete();
    }
}