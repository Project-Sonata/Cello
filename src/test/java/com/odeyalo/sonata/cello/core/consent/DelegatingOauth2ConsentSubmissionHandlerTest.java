package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.MockOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.RedirectUriProvider;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

class DelegatingOauth2ConsentSubmissionHandlerTest {

    @Test
    void shouldReturnRedirectToAuthorizeOnSuccess() {
        // given
        CookieServerRequestCache serverRequestCache = new CookieServerRequestCache();
        serverRequestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange initialExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/hello")
        );

        serverRequestCache.saveRequest(initialExchange).block();

        DelegatingOauth2ConsentSubmissionHandler testable = new DelegatingOauth2ConsentSubmissionHandler(
                new FormDataConsentDecisionResolver(),
                new RequestCacheRedirectingOauth2ConsentApprovedHandler(serverRequestCache),
                new DefaultOauth2ConsentDeniedHandler()
        );

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .cookie(initialExchange.getResponse().getCookies().getFirst("REDIRECT_URI"))
                        .body("action=approved")
        );
        // when
        testable.handleConsentSubmission(MockOauth2AuthorizationRequest.create(), webExchange).block();
        // then
        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(webExchange.getResponse().getHeaders().getLocation()).isEqualTo(initialExchange.getRequest().getURI());
    }

    @Test
    void shouldReturnRedirectWithErrorOnFailed() throws URISyntaxException {
        // given
        CookieServerRequestCache serverRequestCache = new CookieServerRequestCache();
        serverRequestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange initialExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/hello")
        );

        serverRequestCache.saveRequest(initialExchange).block();

        DelegatingOauth2ConsentSubmissionHandler testable = new DelegatingOauth2ConsentSubmissionHandler(
                new FormDataConsentDecisionResolver(),
                new RequestCacheRedirectingOauth2ConsentApprovedHandler(serverRequestCache),
                new DefaultOauth2ConsentDeniedHandler()
        );

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .cookie(initialExchange.getResponse().getCookies().getFirst("REDIRECT_URI"))
                        .body("action=denied")
        );
        // when
        testable.handleConsentSubmission(new RedirectUriRequest(RedirectUri.create("http://localhost:3000/callback")), webExchange).block();
        // then
        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);

        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        assertThat(redirectLocation).isNotNull();

        assertThat(toUriWithoutQueryParams(redirectLocation)).isEqualTo(URI.create("http://localhost:3000/callback"));

        assertThat(redirectLocation).hasParameter("error", "access_denied");
    }

    @NotNull
    private static URI toUriWithoutQueryParams(URI redirectLocation) throws URISyntaxException {
        return new URI(redirectLocation.getScheme(),
                redirectLocation.getAuthority(),
                redirectLocation.getPath(),
                null, // Ignore the query part of the input url
                redirectLocation.getFragment());
    }


    @Value
    private static class RedirectUriRequest implements Oauth2AuthorizationRequest, RedirectUriProvider {
        RedirectUri redirectUri;
    }
}