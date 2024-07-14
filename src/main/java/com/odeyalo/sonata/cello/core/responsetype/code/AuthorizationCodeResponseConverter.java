package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.RedirectUri;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public final class AuthorizationCodeResponseConverter implements Oauth2AuthorizationResponseConverter {

    @Override
    @NotNull
    public Mono<ServerHttpResponse> convert(@NotNull final Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest> response,
                                            @NotNull final ServerWebExchange currentExchange) {

        if ( !(response instanceof AuthorizationCodeResponse) ) {
            return Mono.empty();
        }

        final ServerHttpResponse httpResponse = currentExchange.getResponse();
        final RedirectUri redirectUri = response.getAssociatedRequest().getRedirectUri();


        httpResponse.setStatusCode(HttpStatus.FOUND);
        httpResponse.getHeaders().setLocation(redirectUri.asUri());

        return Mono.just(
                httpResponse
        );
    }
}
