package com.odeyalo.sonata.cello.core.responsetype.code;

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

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.STATE;

public final class AuthorizationCodeResponseConverter implements Oauth2AuthorizationResponseConverter {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull final Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> response,
                                            @NotNull final ServerWebExchange currentExchange) {

        if ( !(response instanceof AuthorizationCodeResponse) ) {
            return Mono.empty();
        }

        final ServerHttpResponse httpResponse = currentExchange.getResponse();
        final Oauth2AuthorizationRequest authorizationRequest = response.getAssociatedRequest();

        final URI redirectUri = UriComponentsBuilder.fromUri(authorizationRequest.getRedirectUri().asUri())
                .queryParam(STATE, authorizationRequest.getState())
                .queryParam("code", "mocked")
                .build()
                .toUri();

        httpResponse.setStatusCode(HttpStatus.FOUND);
        httpResponse.getHeaders().setLocation(redirectUri);

        return Mono.just(httpResponse);
    }
}
