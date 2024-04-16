package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.*;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import testing.RedirectUriOnlyAuthorizationRequest;
import testing.UriAssert;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultOauth2ConsentDeniedHandlerTest {

    @Test
    void shouldReturnRedirectStatus() {
        var testable = new DefaultOauth2ConsentDeniedHandler();
        // given
        var webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent").build()
        );

        var authorizationRequest = new RedirectUriOnlyAuthorizationRequest(
                RedirectUri.create("http://localhost:3000/callback")
        );
        // when
        testable.onConsentDenied(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"), new DeniedConsentDecision(), webExchange).block();
        // then
        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    void shouldReturnRedirectToProvidedRedirectUriInAuthorizationRequest() {
        var testable = new DefaultOauth2ConsentDeniedHandler();
        // given
        var webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent").build()
        );

        var authorizationRequest = new RedirectUriOnlyAuthorizationRequest(
                RedirectUri.create("http://localhost:3000/callback")
        );
        // when
        testable.onConsentDenied(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"), new DeniedConsentDecision(), webExchange).block();
        // then
        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).isEqualToWithoutQueryParameters("http://localhost:3000/callback");
    }

    @Test
    void shouldReturnAccessDeniedErrorInUriParams() {
        var testable = new DefaultOauth2ConsentDeniedHandler();
        // given
        var webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent").build()
        );

        var authorizationRequest = new RedirectUriOnlyAuthorizationRequest(
                RedirectUri.create("http://localhost:3000/callback")
        );
        // when
        testable.onConsentDenied(authorizationRequest, ResourceOwner.withPrincipalOnly("odeyalo"), new DeniedConsentDecision(), webExchange).block();
        // then
        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        assertThat(redirectLocation).hasParameter("error", "access_denied");
    }

}