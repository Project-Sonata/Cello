package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.MockOauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import testing.RedirectUriOnlyAuthorizationRequest;
import testing.UriAssert;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class DelegatingOauth2ConsentSubmissionHandlerTest {

    @Test
    void shouldReturnRedirectToSavedRequestOnApproved() {
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

        MockServerWebExchange approvedConsentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .cookie(initialExchange.getResponse().getCookies().getFirst("REDIRECT_URI"))
                        .body("action=approved")
        );
        // when
        testable.handleConsentSubmission(MockOauth2AuthorizationRequest.create(), ResourceOwner.withPrincipalOnly("odeyalo"), approvedConsentExchange).block();
        // then
        assertThat(approvedConsentExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(approvedConsentExchange.getResponse().getHeaders().getLocation()).isEqualTo(initialExchange.getRequest().getURI());
    }

    @Test
    void shouldReturnRedirectWithErrorOnDenied() {
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
        testable.handleConsentSubmission(new RedirectUriOnlyAuthorizationRequest(RedirectUri.create("http://localhost:3000/callback")),
                ResourceOwner.withPrincipalOnly("odeyalo"),
                webExchange)
                .block();

        // then
        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);

        URI redirectLocation = webExchange.getResponse().getHeaders().getLocation();

        UriAssert.assertThat(redirectLocation).isEqualToWithoutQueryParameters("http://localhost:3000/callback");

        assertThat(redirectLocation).hasParameter("error", "access_denied");
    }
}