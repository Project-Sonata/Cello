package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class RedirectingResourceOwnerAuthenticationSuccessHandlerTest {

    @Test
    void shouldRedirectToSavedRequestCache() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/hello").build();
        MockServerWebExchange exchangeToRedirect = MockServerWebExchange.from(request);

        CookieServerRequestCache requestCache = prepareRequestCache(exchangeToRedirect);

        ResponseCookie cookie = exchangeToRedirect.getResponse().getCookies().getFirst("REDIRECT_URI");

        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/login").cookie(cookie).build()
        );

        var testable = new RedirectingResourceOwnerAuthenticationSuccessHandler(requestCache);

        testable.onAuthenticationSuccess(currentExchange,
                new UsernamePasswordAuthenticatedResourceOwnerAuthentication("test", "test", ResourceOwner.withPrincipalOnly("test"))
        ).block();

        assertThat(currentExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(currentExchange.getResponse().getHeaders().getLocation()).isEqualTo(request.getURI());
    }

    @Test
    void shouldReturnToRootIfRequestCacheIsEmpty() {
        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/login").build()
        );

        var testable = new RedirectingResourceOwnerAuthenticationSuccessHandler(new CookieServerRequestCache());

        testable.onAuthenticationSuccess(currentExchange,
                new UsernamePasswordAuthenticatedResourceOwnerAuthentication("test", "test", ResourceOwner.withPrincipalOnly("test"))
        ).block();

        assertThat(currentExchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(currentExchange.getResponse().getHeaders().getLocation()).isEqualTo(URI.create("/"));
    }

    @NotNull
    private static CookieServerRequestCache prepareRequestCache(MockServerWebExchange exchangeToRedirect) {
        CookieServerRequestCache requestCache = new CookieServerRequestCache();
        requestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());
        requestCache.saveRequest(exchangeToRedirect).block();
        return requestCache;
    }
}