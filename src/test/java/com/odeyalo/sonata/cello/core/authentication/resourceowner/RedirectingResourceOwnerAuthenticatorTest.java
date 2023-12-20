package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static com.odeyalo.sonata.cello.core.authentication.resourceowner.RedirectingResourceOwnerAuthenticator.AUTHENTICATION_COOKIE_NAME;

class RedirectingResourceOwnerAuthenticatorTest {

    public static final String REDIRECT_URI_COOKIE_NAME = "REDIRECT_URI";

    @Test
    void shouldReturnHttpRedirectIfCacheContainsRequestAndUserHasBeenAuthenticated() {
        var requestCache = new CookieServerRequestCache();
        requestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange exchangeToRedirect = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        requestCache.saveRequest(exchangeToRedirect).block();

        ResponseCookie cookie = exchangeToRedirect.getResponse().getCookies().getFirst(REDIRECT_URI_COOKIE_NAME);

        RedirectingResourceOwnerAuthenticator testable = new RedirectingResourceOwnerAuthenticator(
                requestCache, (webExchange -> Mono.just(
                new UsernamePasswordAuthenticatedResourceOwnerAuthentication("odeyalo", "password", ResourceOwner.withPrincipalOnly("odeyalo")))
        ));

        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/login").cookie(cookie)
        );

        testable.authenticate(currentExchange)
                .as(StepVerifier::create)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.FOUND &&
                                Objects.equals(response.getHeaders().getLocation(), exchangeToRedirect.getRequest().getURI())
                )
                .verifyComplete();
    }

    @Test
    void shouldSendRedirectWithCookies() {
        var requestCache = new CookieServerRequestCache();
        requestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange exchangeToRedirect = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        requestCache.saveRequest(exchangeToRedirect).block();

        ResponseCookie cookie = exchangeToRedirect.getResponse().getCookies().getFirst(REDIRECT_URI_COOKIE_NAME);

        RedirectingResourceOwnerAuthenticator testable = new RedirectingResourceOwnerAuthenticator(
                requestCache, (webExchange -> Mono.just(
                new UsernamePasswordAuthenticatedResourceOwnerAuthentication("odeyalo", "password", ResourceOwner.withPrincipalOnly("odeyalo")))
        ));

        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/login").cookie(cookie)
        );

        testable.authenticate(currentExchange)
                .as(StepVerifier::create)
                .expectNextMatches(response -> response.getCookies().getFirst(AUTHENTICATION_COOKIE_NAME) != null)
                .verifyComplete();
    }

    @Test
    void shouldReturnBadRequestIfRedirectUriNotPresent() {
        CookieServerRequestCache requestCache = new CookieServerRequestCache();

        RedirectingResourceOwnerAuthenticator testable = new RedirectingResourceOwnerAuthenticator(
                requestCache, (webExchange -> Mono.just(
                new UsernamePasswordAuthenticatedResourceOwnerAuthentication("odeyalo", "password", ResourceOwner.withPrincipalOnly("odeyalo")))
        ));

        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/login")
        );

        testable.authenticate(currentExchange)
                .as(StepVerifier::create)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    void shouldReturnBadRequestIfCredentialsAreWrong() {
        CookieServerRequestCache requestCache = new CookieServerRequestCache();
        requestCache.setSaveRequestMatcher(ServerWebExchangeMatchers.anyExchange());

        MockServerWebExchange exchangeToRedirect = MockServerWebExchange.from(MockServerHttpRequest.get("/hello").build());
        requestCache.saveRequest(exchangeToRedirect).block();

        ResponseCookie cookie = exchangeToRedirect.getResponse().getCookies().getFirst("REDIRECT_URI");

        RedirectingResourceOwnerAuthenticator testable = new RedirectingResourceOwnerAuthenticator(
                requestCache,
                webExchange -> Mono.empty()
        );


        MockServerWebExchange currentExchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .post("/login")
                        .cookie(cookie)
        );

        testable.authenticate(currentExchange)
                .as(StepVerifier::create)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }
}