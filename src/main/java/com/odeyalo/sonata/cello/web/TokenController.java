package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClientAuthenticationAdapter;
import com.odeyalo.sonata.cello.core.grant.AccessTokenRequestConverter;
import com.odeyalo.sonata.cello.core.grant.AccessTokenResponseConverter;
import com.odeyalo.sonata.cello.core.grant.GrantHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/token")
public final class TokenController {
    private final AccessTokenRequestConverter accessTokenRequestConverter;
    private final GrantHandler grantHandler;
    private final AccessTokenResponseConverter accessTokenResponseConverter;

    public TokenController(final AccessTokenRequestConverter accessTokenRequestConverter,
                           final GrantHandler grantHandler,
                           final AccessTokenResponseConverter accessTokenResponseConverter) {
        this.accessTokenRequestConverter = accessTokenRequestConverter;
        this.grantHandler = grantHandler;
        this.accessTokenResponseConverter = accessTokenResponseConverter;
    }

    @PostMapping
    public Mono<Void> handlerAccessTokenRequest(@NotNull final ServerWebExchange exchange) {
        final Mono<Oauth2RegisteredClient> oauth2Client = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(Oauth2RegisteredClientAuthenticationAdapter.class)
                .map(Oauth2RegisteredClientAuthenticationAdapter::getOauth2RegisteredClient)
                .switchIfEmpty(Mono.error(new IllegalStateException("can't load the oauth2 client")));

        return accessTokenRequestConverter.convert(exchange)
                .zipWith(oauth2Client)
                .flatMap(requestClient -> grantHandler.handle(requestClient.getT1(), requestClient.getT2()))
                .flatMap(it -> accessTokenResponseConverter.writeResponse(it, exchange));
    }
}
