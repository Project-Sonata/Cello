package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.CLIENT_ID;
import static java.lang.String.format;

/**
 * {@link AuthorizationRequestCreationStage} implementation, add the only 'client_id' to {@link AuthorizationRequestBuilder}
 */
@Component
public class ClientIdAuthorizationRequestCreationStage implements AuthorizationRequestCreationStage {

    @Override
    @NotNull
    public Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                             @NotNull AuthorizationRequestBuilder prev) {

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String clientId = queryParams.getFirst(CLIENT_ID);

        if ( clientId == null ) {
            return Mono.error(AuthorizationRequestCreationException.withCustomMessage(format("Missed %s parameter", CLIENT_ID)));
        }

        return Mono.just(
                prev.clientId(clientId)
        );
    }
}
