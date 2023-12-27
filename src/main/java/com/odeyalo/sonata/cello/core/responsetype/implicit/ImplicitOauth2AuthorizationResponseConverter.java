package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

public class ImplicitOauth2AuthorizationResponseConverter implements Oauth2AuthorizationResponseConverter {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> response,
                                            @NotNull ServerWebExchange currentExchange) {

        if ( !(response instanceof ImplicitOauth2AuthorizationResponse implicitOauth2AuthorizationResponse) ) {
            return Mono.empty();
        }

        ServerHttpResponse serverHttpResponse = currentExchange.getResponse();
        ImplicitOauth2AuthorizationRequest authorizationRequest = implicitOauth2AuthorizationResponse.getAssociatedRequest();

        String redirectUri = new StringBuilder(authorizationRequest.getRedirectUri().uriString()) // todo
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
