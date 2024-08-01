package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClientAuthenticationAdapter;
import com.odeyalo.sonata.cello.core.client.authentication.Oauth2ClientResolverStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public final class Oauth2ClientAuthenticationFilter implements WebFilter {
    private final Oauth2ClientResolverStrategy oauth2ClientResolverStrategy;

    public Oauth2ClientAuthenticationFilter(final Oauth2ClientResolverStrategy oauth2ClientResolverStrategy) {
        this.oauth2ClientResolverStrategy = oauth2ClientResolverStrategy;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull final ServerWebExchange exchange,
                             @NotNull final WebFilterChain chain) {

        if ( !isAccessTokenEndpoint(exchange) ) {
            return chain.filter(exchange);
        }

        return oauth2ClientResolverStrategy.resolveClient(exchange)
                .map(Oauth2RegisteredClientAuthenticationAdapter::wrapOauth2Client)
                .map(ReactiveSecurityContextHolder::withAuthentication)
                .flatMap(authenticationContext -> chain.filter(exchange).contextWrite(authenticationContext));
    }

    private static boolean isAccessTokenEndpoint(@NotNull final ServerWebExchange exchange) {
        return exchange.getRequest().getPath().pathWithinApplication().toString().equals("/token");
    }
}
