package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

public class ImplicitOauth2AuthorizationResponseConverter implements Oauth2AuthorizationResponseConverter {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> authorizationResponse,
                                            @NotNull ServerWebExchange currentExchange) {

        if ( !(authorizationResponse instanceof ImplicitOauth2AuthorizationResponse implicitOauth2AuthorizationResponse) ) {
            return Mono.empty();
        }

        ServerHttpResponse serverHttpResponse = currentExchange.getResponse();
        ImplicitOauth2AuthorizationRequest authorizationRequest = implicitOauth2AuthorizationResponse.getAssociatedRequest();

        URI redirectUri = UriComponentsBuilder.fromUri(authorizationRequest.getRedirectUri().asUri())
                .queryParam("access_token", implicitOauth2AuthorizationResponse.getAccessToken())
                .queryParam("token_type", implicitOauth2AuthorizationResponse.getTokenType())
                .queryParam("expires_in", implicitOauth2AuthorizationResponse.getExpiresIn())
                .queryParam("state", implicitOauth2AuthorizationResponse.getState())
                .build()
                .toUri();

        serverHttpResponse.setStatusCode(HttpStatus.FOUND);
        serverHttpResponse.getHeaders().setLocation(redirectUri);

        return Mono.just(serverHttpResponse);
    }
}
