package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.RedirectUriProvider;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequest;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        testable.onConsentDenied(authorizationRequest, new DeniedConsentDecision(), webExchange).block();
        // then
        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    void shouldReturnRedirectToProvidedRedirectUriInAuthorizationRequest() throws URISyntaxException {
        var testable = new DefaultOauth2ConsentDeniedHandler();
        // given
        var webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent").build()
        );

        var authorizationRequest = new RedirectUriOnlyAuthorizationRequest(
                RedirectUri.create("http://localhost:3000/callback")
        );
        // when
        testable.onConsentDenied(authorizationRequest, new DeniedConsentDecision(), webExchange).block();
        // then
        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        assertThat(new URI(redirectLocation.getScheme(),
                redirectLocation.getAuthority(),
                redirectLocation.getPath(),
                null, // Ignore the query part of the input url
                redirectLocation.getFragment())
                .toString()
        ).isEqualTo("http://localhost:3000/callback");
    }

    @Test
    void shouldReturnAccessDeniedErrorInUriParams() throws URISyntaxException {
        var testable = new DefaultOauth2ConsentDeniedHandler();
        // given
        var webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent").build()
        );

        var authorizationRequest = new RedirectUriOnlyAuthorizationRequest(
                RedirectUri.create("http://localhost:3000/callback")
        );
        // when
        testable.onConsentDenied(authorizationRequest, new DeniedConsentDecision(), webExchange).block();
        // then
        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        assertThat(redirectLocation).hasParameter("error", "access_denied");
    }

    @Value
    private static class RedirectUriOnlyAuthorizationRequest implements Oauth2AuthorizationRequest, RedirectUriProvider {
        RedirectUri redirectUri;
    }
}