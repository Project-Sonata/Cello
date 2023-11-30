package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.ResourceOwnerAuthenticationFilter;
import com.odeyalo.sonata.cello.core.authentication.support.FormDataResourceOwnerAuthenticationConverter;
import com.odeyalo.sonata.cello.support.http.ServerWebExchangeReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ServerWebExchangeReactiveHttpResponse;
import com.odeyalo.sonata.cello.support.http.redirect.RequestRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;

@Controller
public class Oauth2Controller {
    @Autowired
    FormDataResourceOwnerAuthenticationConverter converter;
    @Autowired
    RequestRedirectStrategy requestRedirectStrategy;
    @Autowired
    ResourceOwnerAuthenticationFilter filter;

    @GetMapping(value = "/authorize")
    public Mono<String> handleAuthorize(AuthorizationRequest request) throws IOException {

        return Mono.just("login");
    }

    @PostMapping(value = "/login")
    public Mono<Void> login(@RequestParam(value = "continue", required = false) String location, ServerWebExchange exchange) throws IOException {

        return filter.doFilter(new ServerWebExchangeReactiveHttpRequest(exchange), new ServerWebExchangeReactiveHttpResponse(exchange))
                .flatMap(unused -> Mono
                        .deferContextual(ctx -> Mono.just(ctx.getOrDefault("resource_owner", "not exist"))
                        .log()
                        .then())
                );

//        return requestRedirectStrategy.sendRedirect(
//                new ServerWebExchangeReactiveHttpRequest(exchange),
//                new ServerWebExchangeReactiveHttpResponse(exchange),
//                URI.create(location)
//        );
    }
}
// authorize -> user unauthenticated -> redirect to login page -> user authenticated -> cookie has been set -> redirect to authorize -> authorization granted
// authorize -> user authenticated -> authorization granted
