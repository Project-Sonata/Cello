package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.exception.MalformedAccessTokenRequestException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

/**
 * Tries to convert the given {@link ServerWebExchange} to {@link AuthorizationCodeAccessTokenRequest},
 * if grant type in request is missing OR NOT authorization_code, then skip it.
 * If grant type is authorization code, then construct {@link AuthorizationCodeAccessTokenRequest}
 * or return an error if required param is missing
 */
public final class AuthorizationCodeAccessTokenRequestConverter implements AccessTokenRequestConverter {

    @Override
    @NotNull
    public Mono<AccessTokenRequest> convert(@NotNull final ServerWebExchange httpExchange) {
        return httpExchange.getFormData()
                .flatMap(form -> tryParseForm(form));
    }

    @NotNull
    private static Mono<? extends AccessTokenRequest> tryParseForm(@NotNull final MultiValueMap<String, String> form) {
        final String grantType = form.getFirst(GRANT_TYPE);

        if ( GrantType.fromRequestParam(grantType) != GrantType.AUTHORIZATION_CODE ) {
            return Mono.empty();
        }

        final String code = form.getFirst(AUTHORIZATION_CODE);

        if ( code == null ) {
            return Mono.error(new MalformedAccessTokenRequestException("Missing authorization code parameter"));
        }

        final String redirectUri = form.getFirst(REDIRECT_URI);

        if ( redirectUri == null ) {
            return Mono.error(new MalformedAccessTokenRequestException("Missing 'redirect_uri' parameter"));
        }

        return Mono.just(
                new AuthorizationCodeAccessTokenRequest(code, RedirectUri.create(redirectUri))
        );
    }
}
