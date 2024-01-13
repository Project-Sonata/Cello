package com.odeyalo.sonata.cello.spring.configuration.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Match only requests that starts from {@link #ALLOWED_PREFIX}
 */
public class CelloServerWebExchangeMatcher implements ServerWebExchangeMatcher {
    private static final String ALLOWED_PREFIX = "/oauth2";

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        boolean isAllowed = StringUtils.startsWith(exchange.getRequest().getPath().value(), ALLOWED_PREFIX);
        return isAllowed ? MatchResult.match() : MatchResult.notMatch();
    }
}
