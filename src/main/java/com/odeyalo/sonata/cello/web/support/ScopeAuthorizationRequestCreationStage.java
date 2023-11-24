package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest.AuthorizationRequestBuilder;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.exception.AuthorizationRequestCreationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.SCOPE;
import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.STATE;
import static java.lang.String.format;

/**
 * {@link AuthorizationRequestCreationStage} implementation, add the only 'scope' to {@link AuthorizationRequestBuilder}
 */
@Component
public class ScopeAuthorizationRequestCreationStage implements AuthorizationRequestCreationStage {
    private static final String SCOPE_DELIMITER = " ";

    @Override
    @NotNull
    public Mono<AuthorizationRequestBuilder> processCreation(@NotNull ServerWebExchange exchange,
                                                             @NotNull AuthorizationRequestBuilder prev) {

        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();

        String scope = queryParams.getFirst(SCOPE);

        if ( scope == null ) {
            return Mono.error(AuthorizationRequestCreationException.withCustomMessage(format("Missed %s parameter", STATE)));
        }

        List<SimpleScope> scopes = parseScopes(scope);

        return Mono.just(
                prev.scopes(ScopeContainer.fromCollection(scopes))
        );
    }

    @NotNull
    private static List<SimpleScope> parseScopes(String scope) {
        String[] separatedScopes = StringUtils.split(scope, SCOPE_DELIMITER);

        return Arrays.stream(separatedScopes)
                .map(SimpleScope::withName)
                .toList();
    }
}
