package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * Handle only Authorization code response type as defined in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1">Authorization Code Grant</a>
 */
@Component
public final class AuthorizationCodeResponseTypeHandler implements Oauth2ResponseTypeHandler {
    private final AuthorizationCodeGenerator authorizationCodeGenerator;

    public AuthorizationCodeResponseTypeHandler(final AuthorizationCodeGenerator authorizationCodeGenerator) {
        this.authorizationCodeGenerator = authorizationCodeGenerator;
    }

    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull final Oauth2AuthorizationRequest authorizationRequest) {
        return Mono.just(
                authorizationRequest instanceof AuthorizationCodeRequest
        );
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest>> permissionGranted(@NotNull final Oauth2AuthorizationRequest authorizationRequest,
                                                                                                     @NotNull final ResourceOwner resourceOwner) {

        final AuthorizationCodeRequest authorizationCodeRequest = (AuthorizationCodeRequest) authorizationRequest;

        return authorizationCodeGenerator.newAuthorizationCode()
                .map(code -> AuthorizationCodeResponse.withAssociatedRequest(authorizationCodeRequest)
                        .authorizationCode(code.value())
                        .build()
                );
    }
}
