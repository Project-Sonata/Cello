package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Redirect the user to saved redirect uri in {@link ServerRequestCache} after successful authentication, otherwise return HTTP 400 BAD Request as response.
 *
 */
@Component
public class RedirectingResourceOwnerAuthenticator implements ResourceOwnerAuthenticator {
    private final ServerRequestCache serverRequestCache;
    private final ResourceOwnerAuthenticationManager authenticationManager;

    public static final String AUTHENTICATION_COOKIE_NAME = "clid";

    public RedirectingResourceOwnerAuthenticator(ServerRequestCache serverRequestCache, ResourceOwnerAuthenticationManager authenticationManager) {
        this.serverRequestCache = serverRequestCache;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<ServerHttpResponse> authenticate(ServerWebExchange webExchange) {
        ServerHttpResponse response = webExchange.getResponse();
        response.setRawStatusCode(400);

        return authenticationManager.attemptAuthentication(webExchange)
                .flatMap(authentication -> serverRequestCache.getRedirectUri(webExchange))
                .map(uri -> {

                    response.addCookie(
                            ResponseCookie.from(AUTHENTICATION_COOKIE_NAME, "odeyalo").build()
                    );

                    response.getHeaders().setLocation(uri);
                    response.setStatusCode(HttpStatus.FOUND);
                    return response;
                })
                .defaultIfEmpty(response);


    }
}
