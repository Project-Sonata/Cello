package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticator;
import com.odeyalo.sonata.cello.spring.auth.CelloOauth2CookieResourceOwnerAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@Controller
public class Oauth2Controller {

    private final ResourceOwnerAuthenticator resourceOwnerAuthenticationManager;
    private final AuthenticationPageProvider authenticationPageProvider;

    public Oauth2Controller(ResourceOwnerAuthenticator resourceOwnerAuthenticationManager,
                            AuthenticationPageProvider authenticationPageProvider) {

        this.resourceOwnerAuthenticationManager = resourceOwnerAuthenticationManager;
        this.authenticationPageProvider = authenticationPageProvider;
    }

    @GetMapping(value = "/authorize")
    @ResponseBody
    public Mono<ResponseEntity<String>> handleAuthorize(AuthorizationRequest request,
                                                        @AuthenticationPrincipal CelloOauth2CookieResourceOwnerAuthentication token) {

        return Mono.just(
                        ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
                                .body("You are: " + token.getPrincipal() + " Permissions: " + request.getScopes())

                )
                .log("Cello-Oauth2-Resource-Owner-Auth", Level.FINE);
    }


    @GetMapping(value = "/login")
    public Mono<Void> handleLogin(ServerWebExchange exchange) {

        return authenticationPageProvider.getAuthenticationPage(exchange)
                .then();
    }


    @PostMapping(value = "/login")
    public Mono<ServerHttpResponse> handleLoginSubmission(ServerWebExchange exchange) {
        return resourceOwnerAuthenticationManager.authenticate(exchange)
                .log("Cello-Oauth2-Resource-Owner-Auth", Level.FINE);
    }
}
