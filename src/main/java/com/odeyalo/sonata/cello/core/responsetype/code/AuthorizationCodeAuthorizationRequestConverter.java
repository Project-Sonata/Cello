package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.*;
import com.odeyalo.sonata.cello.exception.MalformedOauth2RequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

public final class AuthorizationCodeAuthorizationRequestConverter implements Oauth2AuthorizationRequestConverter {

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationRequest> convert(@NotNull final ServerWebExchange exchange) {
        final MultiValueMap<String, String> requestParameters = exchange.getRequest().getQueryParams();

        final Oauth2ResponseType responseType = Oauth2ResponseType.create(
                requestParameters.getFirst(Oauth2RequestParameters.RESPONSE_TYPE)
        );

        if ( !Objects.equals(responseType, DefaultOauth2ResponseTypes.AUTHORIZATION_CODE) ) {
            return Mono.empty();
        }

        final var builder = AuthorizationCodeRequest.builder();

        final String clientId = requestParameters.getFirst(Oauth2RequestParameters.CLIENT_ID);

        if ( clientId == null ) {
            return Mono.error(
                    MalformedOauth2RequestException.withCustomMessage("Missed client_id parameter")
            );
        }

        final String redirectUri = requestParameters.getFirst(Oauth2RequestParameters.REDIRECT_URI);

        if ( redirectUri == null ) {
            return Mono.error(
                    MalformedOauth2RequestException.withCustomMessage("Missed redirect_uri parameter")
            );
        }

        final String state = requestParameters.getFirst(Oauth2RequestParameters.STATE);


        final String scopesStr = requestParameters.getFirst(Oauth2RequestParameters.SCOPE);

        return Mono.just(
                builder.clientId(clientId)
                        .redirectUri(RedirectUri.create(redirectUri))
                        .scopes(ScopeContainer.fromOauth2String(scopesStr))
                        .state(state)
                        .build()
        );
    }
}
