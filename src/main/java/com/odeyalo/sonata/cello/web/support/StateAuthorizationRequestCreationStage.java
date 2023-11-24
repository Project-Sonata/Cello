package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.STATE;
import static java.lang.String.format;

/**
 * {@link AuthorizationRequestCreationStage} implementation, add the only 'state' to {@link AuthorizationRequestBuilder}
 */
@Component
public class StateAuthorizationRequestCreationStage implements AuthorizationRequestCreationStage {

    @Override
    @NotNull
    public Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                             @NotNull AuthorizationRequestBuilder prev) {

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String state = queryParams.getFirst(STATE);

        if ( state == null ) {
            return Mono.error(AuthorizationRequestCreationException.withCustomMessage(format("Missed %s parameter", STATE)));
        }

        return Mono.just(
                prev.state(state)
        );
    }
}
