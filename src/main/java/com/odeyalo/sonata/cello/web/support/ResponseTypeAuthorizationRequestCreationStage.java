package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.RESPONSE_TYPE;

/**
 * {@link AuthorizationRequestCreationStage} implementation, add the only 'response_type' to {@link AuthorizationRequestBuilder}
 */
@Component
public class ResponseTypeAuthorizationRequestCreationStage implements AuthorizationRequestCreationStage {

    @Override
    @NotNull
    public Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                             @NotNull AuthorizationRequestBuilder prev) {

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String responseType = queryParams.getFirst(RESPONSE_TYPE);

        if ( responseType == null ) {
            return Mono.error(AuthorizationRequestCreationException.withCustomMessage("Missed response_type parameter"));
        }

        return Mono.just(
                prev.responseType(responseType)
        );
    }
}
