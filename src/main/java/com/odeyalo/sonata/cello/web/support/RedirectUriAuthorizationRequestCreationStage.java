package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.REDIRECT_URI;
import static java.lang.String.format;

/**
 * {@link AuthorizationRequestCreationStage} implementation, add the only 'redirect_uri' to {@link AuthorizationRequestBuilder}
 */
@Component
public class RedirectUriAuthorizationRequestCreationStage implements AuthorizationRequestCreationStage {

    @Override
    @NotNull
    public Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                             @NotNull AuthorizationRequestBuilder prev) {

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String redirectUri = queryParams.getFirst(REDIRECT_URI);

        if ( redirectUri == null ) {
            return Mono.error(AuthorizationRequestCreationException.withCustomMessage(format("Missed %s parameter", REDIRECT_URI)));
        }

        return Mono.just(
                prev.redirectUri(redirectUri)
        );
    }
}
