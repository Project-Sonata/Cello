package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticator;
import com.odeyalo.sonata.cello.spring.auth.CelloOauth2CookieResourceOwnerAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Oauth2AuthorizationResponseConverter converter;
    private final Oauth2ResponseTypeHandler oauth2ResponseTypeHandler;

    public Oauth2Controller(ResourceOwnerAuthenticator resourceOwnerAuthenticationManager,
                            AuthenticationPageProvider authenticationPageProvider, Oauth2AuthorizationResponseConverter converter, Oauth2ResponseTypeHandler oauth2ResponseTypeHandler) {

        this.resourceOwnerAuthenticationManager = resourceOwnerAuthenticationManager;
        this.authenticationPageProvider = authenticationPageProvider;
        this.converter = converter;
        this.oauth2ResponseTypeHandler = oauth2ResponseTypeHandler;
    }

    @GetMapping(value = "/authorize")
    @ResponseBody
    public Mono<Void> handleAuthorize(Oauth2AuthorizationRequest request,
                                      ServerWebExchange exchange,
                                      @AuthenticationPrincipal CelloOauth2CookieResourceOwnerAuthentication token) {

        return oauth2ResponseTypeHandler.permissionGranted(request, ResourceOwner.withPrincipalOnly("odeyalo"))
                .flatMap(response -> converter.convert(
                        response,
                        exchange
                )).then()
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
