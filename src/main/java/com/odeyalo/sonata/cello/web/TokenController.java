package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.grant.AccessTokenRequestConverter;
import com.odeyalo.sonata.cello.core.grant.AccessTokenResponseConverter;
import com.odeyalo.sonata.cello.core.grant.AuthorizationCodeAccessTokenRequest;
import com.odeyalo.sonata.cello.core.grant.GrantHandler;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/token")
public final class TokenController {

    @Autowired
    AccessTokenRequestConverter accessTokenRequestConverter;

    @Autowired
    AuthorizationCodeService authorizationCodeService;

    @Autowired
    GrantHandler grantHandler;

    @Autowired
    AccessTokenResponseConverter accessTokenResponseConverter;

    @PostMapping
    public Mono<Void> handlerAccessTokenRequest(@NotNull final ServerWebExchange exchange) {
        return accessTokenRequestConverter.convert(exchange)
                .cast(AuthorizationCodeAccessTokenRequest.class)
                .flatMap(request -> grantHandler.handle(request))
                .flatMap(it -> accessTokenResponseConverter.writeResponse(it, exchange));
    }
}
