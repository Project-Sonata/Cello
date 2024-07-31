package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.grant.AccessTokenRequestConverter;
import com.odeyalo.sonata.cello.core.grant.AuthorizationCodeAccessTokenRequest;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeService;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerationContext;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("/token")
public final class TokenController {
    @Autowired
    AuthorizationCodeService authorizationCodeService;
    @Autowired
    Oauth2AccessTokenGenerator accessTokenGenerator;

    @Autowired
    AccessTokenRequestConverter accessTokenRequestConverter;

    @PostMapping
    public Mono<ResponseEntity<AuthorizationCodeAccessTokenResponse>> handlerAccessTokenRequest(@NotNull final ServerWebExchange exchange) {
        return accessTokenRequestConverter.convert(exchange)
                .cast(AuthorizationCodeAccessTokenRequest.class)
                .map(AuthorizationCodeAccessTokenRequest::getAuthorizationCode)
                .flatMap(code -> authorizationCodeService.loadUsing(code))
                .map(generated -> Oauth2AccessTokenGenerationContext.builder()
                        .scopes(generated.getRequestedScopes())
                        .client(generated.getGrantedFor())
                        .resourceOwner(generated.getGrantedBy())
                        .build())
                .flatMap(context -> accessTokenGenerator.generateToken(context))
                .map(token -> new AuthorizationCodeAccessTokenResponse(
                        token.getTokenValue(),
                        (int) (token.getExpiresIn().getEpochSecond() - Instant.now().getEpochSecond()),
                        token.getTokenType().typeName()))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()))
                .log("code");
    }
}
