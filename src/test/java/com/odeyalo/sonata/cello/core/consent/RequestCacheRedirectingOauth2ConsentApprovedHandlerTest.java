package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import static org.assertj.core.api.Assertions.assertThat;

class RequestCacheRedirectingOauth2ConsentApprovedHandlerTest {

    @Test
    void shouldReturnRedirectStatusCode() {
        CookieServerRequestCache cookieServerRequestCache = new CookieServerRequestCache();
        cookieServerRequestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange initialExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/hello")
        );

        cookieServerRequestCache.saveRequest(initialExchange).block();

        RequestCacheRedirectingOauth2ConsentApprovedHandler testable = new RequestCacheRedirectingOauth2ConsentApprovedHandler(
                cookieServerRequestCache
        );
        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent")
                        .cookie(initialExchange.getResponse().getCookies().getFirst("REDIRECT_URI"))
                        .build()
        );

        testable.onConsentApproved(new MockAuthorizationRequest(), new ApprovedConsentDecision(), webExchange).block();

        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    @Test
    void shouldReturnRedirectToSavedURI() {
        CookieServerRequestCache cookieServerRequestCache = new CookieServerRequestCache();
        cookieServerRequestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange initialExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/hello")
        );

        cookieServerRequestCache.saveRequest(initialExchange).block();

        RequestCacheRedirectingOauth2ConsentApprovedHandler testable = new RequestCacheRedirectingOauth2ConsentApprovedHandler(
                cookieServerRequestCache
        );

        MockServerWebExchange webExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/oauth2/consent")
                        .cookie(initialExchange.getResponse().getCookies().getFirst("REDIRECT_URI"))
                        .build()
        );

        testable.onConsentApproved(new MockAuthorizationRequest(), new ApprovedConsentDecision(), webExchange).block();

        assertThat(webExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }


    private static class MockAuthorizationRequest implements Oauth2AuthorizationRequest {
    }
}