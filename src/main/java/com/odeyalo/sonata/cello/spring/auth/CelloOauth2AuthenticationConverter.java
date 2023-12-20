package com.odeyalo.sonata.cello.spring.auth;

import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link ServerWebExchange} to {@link CelloOauth2CookieResourceOwnerAuthentication}
 * if and only if {@value  CELLO_LOGIN_COOKIE_NAME} cookie present.
 */
@Component
public class CelloOauth2AuthenticationConverter implements ServerAuthenticationConverter {
    private static final String CELLO_LOGIN_COOKIE_NAME = "clid";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst(CELLO_LOGIN_COOKIE_NAME);

        if ( cookie == null ) {
            return Mono.empty();
        }

        return Mono.just(
                CelloOauth2CookieResourceOwnerAuthentication.unauthenticated(cookie.getValue())
        );
    }
}
