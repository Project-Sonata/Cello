package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.exception.MissingAuthorizationRequestFlowIdException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.RequestPath;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME;

/**
 * Populate the {@link ServerWebExchange} attributes with the flow ID
 */
public class Oauth2FlowAttributePopulatingFilter implements WebFilter {

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull ServerWebExchange exchange,
                             @NotNull WebFilterChain chain) {

        RequestPath path = exchange.getRequest().getPath();

        if ( Objects.equals(path.pathWithinApplication().toString(), "/authorize") ) {
            // Staring the oauth2 flow, generate the flow id and continue to execute chain
            exchange.getAttributes().put(CURRENT_FLOW_ATTRIBUTE_NAME, UUID.randomUUID().toString());
            return chain.filter(exchange);
        }

        String flowId = exchange.getRequest().getQueryParams().getFirst("flow_id");

        if ( flowId == null ) {
            return Mono.error(MissingAuthorizationRequestFlowIdException.withCustomMessage("Missing 'flow_id' parameter!"));
        }

        return Mono.fromRunnable(
                () -> exchange.getAttributes().put(CURRENT_FLOW_ATTRIBUTE_NAME, flowId)
        ).then(chain.filter(exchange));
    }
}
