package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticator;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentSubmissionHandler;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    private final Oauth2ConsentPageProvider oauth2ConsentPageProvider;
    private final Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler;

    public Oauth2Controller(ResourceOwnerAuthenticator resourceOwnerAuthenticationManager,
                            AuthenticationPageProvider authenticationPageProvider, Oauth2AuthorizationResponseConverter converter, Oauth2ResponseTypeHandler oauth2ResponseTypeHandler, Oauth2ConsentPageProvider oauth2ConsentPageProvider, Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler) {

        this.resourceOwnerAuthenticationManager = resourceOwnerAuthenticationManager;
        this.authenticationPageProvider = authenticationPageProvider;
        this.converter = converter;
        this.oauth2ResponseTypeHandler = oauth2ResponseTypeHandler;
        this.oauth2ConsentPageProvider = oauth2ConsentPageProvider;
        this.oauth2ConsentSubmissionHandler = oauth2ConsentSubmissionHandler;
    }

    @GetMapping(value = "/authorize")
    @ResponseBody
    public Mono<Void> handleAuthorize() {

        return Mono.empty();
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

    @GetMapping("/oauth2/consent")
    public Mono<Void> getConsentPage(Oauth2AuthorizationRequest request, ServerWebExchange exchange) {

        return oauth2ConsentPageProvider.getConsentPage(request, ResourceOwner.withPrincipalOnly("odeyalo"), exchange);
    }

    @PostMapping("/oauth2/consent")
    public Mono<Void> handleConsentSubmission(Oauth2AuthorizationRequest oauth2AuthorizationRequest, ServerWebExchange webExchange) {
        return oauth2ConsentSubmissionHandler.handleConsentSubmission(oauth2AuthorizationRequest, webExchange);
    }
}
