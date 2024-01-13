package com.odeyalo.sonata.cello.core.responsetype.implicit;

import com.odeyalo.sonata.cello.core.*;
import com.odeyalo.sonata.cello.exception.MalformedOauth2RequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Convert the {@link ServerWebExchange} to {@link ImplicitOauth2AuthorizationRequest}
 */
public class ImplicitOauth2AuthorizationRequestConverter implements Oauth2AuthorizationRequestConverter {

    public static final String SCOPES_SPLITERATOR = " ";

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationRequest> convert(@NotNull ServerWebExchange exchange) {
        MultiValueMap<String, String> requestParameters = exchange.getRequest().getQueryParams();

        Oauth2ResponseType responseType = Oauth2ResponseType.create(
                requestParameters.getFirst(Oauth2RequestParameters.RESPONSE_TYPE)
        );

        if ( !Objects.equals(responseType, DefaultOauth2ResponseTypes.IMPLICIT) ) {
            return Mono.empty();
        }

        var builder = ImplicitOauth2AuthorizationRequest.builder();

        String clientId = requestParameters.getFirst(Oauth2RequestParameters.CLIENT_ID);

        if ( clientId == null ) {
            return Mono.error(
                    MalformedOauth2RequestException.withCustomMessage("Missed client id parameter")
            );
        }

        builder.clientId(clientId);

        String state = requestParameters.getFirst(Oauth2RequestParameters.STATE);

        if ( state != null ) {
            builder.state(state);
        }

        String redirectUri = requestParameters.getFirst(Oauth2RequestParameters.REDIRECT_URI);

        if ( redirectUri == null ) {
            return Mono.error(
                    MalformedOauth2RequestException.withCustomMessage("Missed redirect_uri parameter")
            );
        }

        builder.redirectUri(RedirectUri.create(redirectUri));

        String scopesStr = requestParameters.getFirst(Oauth2RequestParameters.SCOPE);

        if ( scopesStr != null ) {
            List<SimpleScope> scopes = Arrays.stream(scopesStr.split(SCOPES_SPLITERATOR))
                    .map(SimpleScope::withName)
                    .toList();

            builder.scopes(ScopeContainer.fromCollection(scopes));
        }
        return Mono.just(
                builder.build()
        );
    }
}
