package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticator;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentSubmissionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.logging.Level;

@Controller
@RequestMapping("/oauth2")
public class Oauth2Controller {

    private final ResourceOwnerAuthenticator resourceOwnerAuthenticationManager;
    private final AuthenticationPageProvider authenticationPageProvider;

    private final Oauth2ConsentPageProvider oauth2ConsentPageProvider;
    private final Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler;

    public Oauth2Controller(ResourceOwnerAuthenticator resourceOwnerAuthenticationManager,
                            AuthenticationPageProvider authenticationPageProvider,
                            Oauth2ConsentPageProvider oauth2ConsentPageProvider,
                            Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler) {

        this.resourceOwnerAuthenticationManager = resourceOwnerAuthenticationManager;
        this.authenticationPageProvider = authenticationPageProvider;
        this.oauth2ConsentPageProvider = oauth2ConsentPageProvider;
        this.oauth2ConsentSubmissionHandler = oauth2ConsentSubmissionHandler;
    }

    @GetMapping(value = "/authorize")
    @ResponseBody
    public Mono<ResponseEntity<Void>> handleAuthorize(ServerWebExchange webExchange) {

        String flowId = webExchange.getAttribute(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME);

        return Mono.just(
                ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, "/oauth2/consent?flow_id=" + flowId)
                        .build()
        );
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

    @GetMapping("/consent")
    public Mono<Void> getConsentPage(Oauth2AuthorizationRequest request, ServerWebExchange exchange) {

        return oauth2ConsentPageProvider.getConsentPage(request, ResourceOwner.withPrincipalOnly("odeyalo"), exchange);
    }

    @GetMapping("/login/{providerName}")
    public Mono<ResponseEntity<Void>> thirdPartyAuthenticationProvider(Oauth2AuthorizationRequest request,
                                                                       ServerWebExchange exchange,
                                                                       @PathVariable String providerName) {
        if ( !Objects.equals(providerName, "google") ) {
            return Mono.just(
                    ResponseEntity.badRequest().build()
            );
        }
        return Mono.just(
                ResponseEntity.status(302)
                        .header(HttpHeaders.LOCATION, "https://accounts.google.com/o/oauth2/v2/auth?client_id=123&redirect_uri=localhost:3000")
                        .build()
        );
    }

    @PostMapping("/consent")
    public Mono<Void> handleConsentSubmission(Oauth2AuthorizationRequest oauth2AuthorizationRequest, AuthenticatedResourceOwnerAuthentication authentication, ServerWebExchange webExchange) {
        ResourceOwner resourceOwner = authentication.getResourceOwner();
        return oauth2ConsentSubmissionHandler.handleConsentSubmission(oauth2AuthorizationRequest, resourceOwner, webExchange);
    }
}
