package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2AuthenticationManager;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwnerAuthenticator;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentPageProvider;
import com.odeyalo.sonata.cello.core.consent.Oauth2ConsentSubmissionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@Controller
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {
    private final ResourceOwnerAuthenticator resourceOwnerAuthenticationManager;
    private final AuthenticationPageProvider authenticationPageProvider;
    private final Oauth2ConsentPageProvider oauth2ConsentPageProvider;
    private final Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler;
    private final Oauth2AuthenticationManager oauth2AuthenticationManager;

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
    public Mono<Void> getConsentPage(Oauth2AuthorizationRequest request, AuthenticatedResourceOwnerAuthentication user, ServerWebExchange exchange) {

        return oauth2ConsentPageProvider.getConsentPage(request, user.getResourceOwner(), exchange);
    }

    @GetMapping("/login/{providerName}")
    public Mono<Void> thirdPartyAuthenticationProvider(@PathVariable String providerName, ServerWebExchange webExchange) {
        return oauth2AuthenticationManager.startOauth2Authentication(providerName, webExchange);
    }

    @GetMapping("/login/{providerName}/callback")
    public Mono<Void> thirdPartyAuthenticationProviderCallback(@PathVariable String providerName, ServerWebExchange exchange) {
        return resourceOwnerAuthenticationManager.authenticate(exchange).then();
    }

    @PostMapping("/consent")
    public Mono<Void> handleConsentSubmission(Oauth2AuthorizationRequest oauth2AuthorizationRequest, AuthenticatedResourceOwnerAuthentication authentication, ServerWebExchange webExchange) {
        ResourceOwner resourceOwner = authentication.getResourceOwner();
        return oauth2ConsentSubmissionHandler.handleConsentSubmission(oauth2AuthorizationRequest, resourceOwner, webExchange);
    }
}
