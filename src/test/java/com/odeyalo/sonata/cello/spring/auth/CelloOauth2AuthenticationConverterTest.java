package com.odeyalo.sonata.cello.spring.auth;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ClassUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpCookie;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.util.Objects;

class CelloOauth2AuthenticationConverterTest {

    public static final String CELLO_LOGIN_ID_COOKIE_NAME = "clid";

    @Test
    void shouldReturnSpecificAuthenticationOnCookiePresent() {
        CelloOauth2AuthenticationConverter testable = new CelloOauth2AuthenticationConverter();

        var awarenessCookieRequest = MockServerHttpRequest.get("/authorize")
                .cookie(new HttpCookie(CELLO_LOGIN_ID_COOKIE_NAME, "value"))
                .build();
        var exchange = MockServerWebExchange.from(awarenessCookieRequest);

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(authentication -> ClassUtils.isAssignable(authentication.getClass(), CelloOauth2CookieResourceOwnerAuthentication.class))
                .verifyComplete();
    }

    @Test
    void shouldReturnUnauthenticatedAuthenticationOnCookiePresent() {
        CelloOauth2AuthenticationConverter testable = new CelloOauth2AuthenticationConverter();

        var awarenessCookieRequest = MockServerHttpRequest.get("/authorize")
                .cookie(new HttpCookie(CELLO_LOGIN_ID_COOKIE_NAME, "value"))
                .build();
        var exchange = MockServerWebExchange.from(awarenessCookieRequest);

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(authentication -> BooleanUtils.isFalse(authentication.isAuthenticated()))
                .verifyComplete();
    }

    @Test
    void authenticationPrincipalShouldBeEqualToCookieValue() {
        CelloOauth2AuthenticationConverter testable = new CelloOauth2AuthenticationConverter();

        var cookieValue = "odeyalooo";
        var awarenessCookieRequest = MockServerHttpRequest.get("/authorize")
                .cookie(new HttpCookie(CELLO_LOGIN_ID_COOKIE_NAME, cookieValue))
                .build();
        var exchange = MockServerWebExchange.from(awarenessCookieRequest);

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(authentication -> Objects.equals(authentication.getPrincipal(), cookieValue))
                .verifyComplete();
    }

    @Test
    void authenticationCredentialsShouldBeNull() {
        CelloOauth2AuthenticationConverter testable = new CelloOauth2AuthenticationConverter();

        var cookieValue = "odeyalooo";
        var awarenessCookieRequest = MockServerHttpRequest.get("/authorize")
                .cookie(new HttpCookie(CELLO_LOGIN_ID_COOKIE_NAME, cookieValue))
                .build();
        var exchange = MockServerWebExchange.from(awarenessCookieRequest);

        testable.convert(exchange)
                .as(StepVerifier::create)
                .expectNextMatches(authentication -> Objects.isNull(authentication.getCredentials() ))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyOnMissingCookie() {
        CelloOauth2AuthenticationConverter testable = new CelloOauth2AuthenticationConverter();

        var unconsciousCookieRequest = MockServerHttpRequest.get("/authorize").build();
        var exchange = MockServerWebExchange.from(unconsciousCookieRequest);

        testable.convert(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}