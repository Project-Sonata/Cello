package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class ImplicitOauth2AuthorizationResponseConverter implements Oauth2AuthorizationResponseConverter {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull Oauth2AuthorizationExchange exchange,
                                            @NotNull ServerWebExchange currentExchange) {

        if ( !(exchange.response() instanceof ImplicitOauth2AuthorizationResponse implicitOauth2AuthorizationResponse) ) {
            return Mono.empty();
        }
        ServerHttpResponse serverHttpResponse = currentExchange.getResponse();

        AuthorizationRequest authorizationRequest = exchange.request();

        String redirectUri = new StringBuilder(authorizationRequest.getRedirectUri())
                .append("?access_token=")
                .append(implicitOauth2AuthorizationResponse.getAccessToken())
                .append("&token_type=")
                .append(implicitOauth2AuthorizationResponse.getTokenType())
                .append("&expires_in=")
                .append(implicitOauth2AuthorizationResponse.getExpiresIn())
                .append("&state=")
                .append(implicitOauth2AuthorizationResponse.getState())
                .toString();

        serverHttpResponse.setStatusCode(HttpStatus.FOUND);
        serverHttpResponse.getHeaders().setLocation(URI.create(redirectUri));

        return Mono.just(serverHttpResponse);
    }
}
