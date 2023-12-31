package com.odeyalo.sonata.cello.core.consent;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Used to provide the consent page to resource owner
 */
public interface Oauth2ConsentPageProvider {
    /**
     * Return the consent page to the resource owner
     * @param request - current authorization request
     * @param resourceOwner - owner that granted access and should consent
     * @param httpExchange - current http exchange
     * @return {@link Mono} with {@link Void}
     */
    @NotNull
    Mono<Void> getConsentPage(@NotNull Oauth2AuthorizationRequest request,
                              @NotNull ResourceOwner resourceOwner,
                              @NotNull ServerWebExchange httpExchange);

}
