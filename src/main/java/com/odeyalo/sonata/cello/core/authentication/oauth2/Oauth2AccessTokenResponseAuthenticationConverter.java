package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.Oauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticatedResourceOwnerAuthentication;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Strategy to convert a {@link Oauth2AccessTokenResponse} to the {@link AuthenticatedResourceOwnerAuthentication}
 */
public interface Oauth2AccessTokenResponseAuthenticationConverter {
    /**
     * Tries to convert {@link Oauth2AccessTokenResponse} to {@link AuthenticatedResourceOwnerAuthentication}. If the given implementation does not support
     * this type of response, then {@link Mono#empty()} MUST BE returned
     * @param response already parsed response from OAuth 2.0 Provider
     * @return {@link Mono} emitting {@link AuthenticatedResourceOwnerAuthentication} on success, or {@link Mono#empty()} if not supported
     */
    @NotNull
    Mono<AuthenticatedResourceOwnerAuthentication> convertToAuthentication(@NotNull Oauth2AccessTokenResponse response);

}
