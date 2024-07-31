package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.grant.*;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public Mono<ResponseEntity<AuthorizationCodeAccessTokenResponse>> handlerAccessTokenRequest(@NotNull final ServerWebExchange exchange) {
        return accessTokenRequestConverter.convert(exchange)
                .cast(AuthorizationCodeAccessTokenRequest.class)
                .flatMap(request -> grantHandler.handle(request))
                .map(it -> new AuthorizationCodeAccessTokenResponse(it.getAccessTokenValue(),
                        it.getExpiresIn(),
                        it.getTokenType()))
                .map(ResponseEntity::ok);
    }
}
