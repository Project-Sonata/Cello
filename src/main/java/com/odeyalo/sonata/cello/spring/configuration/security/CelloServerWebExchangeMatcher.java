package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.web.CelloOauth2ServerEndpointsSpec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Match only requests that starts from {@link #endpointsSpec}
 */
public class CelloServerWebExchangeMatcher implements ServerWebExchangeMatcher {
    private final CelloOauth2ServerEndpointsSpec endpointsSpec;

    public CelloServerWebExchangeMatcher(CelloOauth2ServerEndpointsSpec endpointsSpec) {
        this.endpointsSpec = endpointsSpec;
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        boolean isAllowed = StringUtils.startsWith(exchange.getRequest().getPath().value(), endpointsSpec.getPrefix());
        return isAllowed ? MatchResult.match() : MatchResult.notMatch();
    }
}
