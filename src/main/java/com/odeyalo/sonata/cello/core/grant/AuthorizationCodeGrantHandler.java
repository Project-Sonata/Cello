package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeMetadata;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeLoader;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessToken;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerationContext;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import com.odeyalo.sonata.cello.exception.AuthorizationCodeStolenException;
import com.odeyalo.sonata.cello.exception.InvalidAuthorizationCodeException;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Handle authorization code flow, exchange EXISTING authorization code for access token.
 * If code does not exist OR expired, then return an error
 */
public final class AuthorizationCodeGrantHandler implements GrantHandler {
    private final AuthorizationCodeLoader authorizationCodeLoader;
    private final Oauth2AccessTokenGenerator accessTokenGenerator;

    public AuthorizationCodeGrantHandler(final AuthorizationCodeLoader authorizationCodeLoader,
                                         final Oauth2AccessTokenGenerator accessTokenGenerator) {
        this.authorizationCodeLoader = authorizationCodeLoader;
        this.accessTokenGenerator = accessTokenGenerator;
    }


    @Override
    @NotNull
    public Mono<Boolean> supports(@NotNull final AccessTokenRequest accessTokenRequest) {
        return Mono.just(
                accessTokenRequest instanceof AuthorizationCodeAccessTokenRequest
        );
    }

    @Override
    @NotNull
    public Mono<AccessTokenResponse> handle(@NotNull final AccessTokenRequest accessTokenRequest,
                                            @NotNull final Oauth2RegisteredClient client) {

        final AuthorizationCodeAccessTokenRequest codeRequest = (AuthorizationCodeAccessTokenRequest) accessTokenRequest;
        final String authorizationCode = codeRequest.getAuthorizationCode();

        return authorizationCodeLoader.loadUsing(authorizationCode)
                .switchIfEmpty(Mono.defer(() -> Mono.error(InvalidAuthorizationCodeException::defaultException)))
                .flatMap(codeMetadata -> validate(client, authorizationCode, codeMetadata))
                .map(AuthorizationCodeGrantHandler::createAccessTokenGenerationContext)
                .flatMap(accessTokenGenerator::generateToken)
                .map(AuthorizationCodeGrantHandler::createResponse);
    }

    @NotNull
    private static Mono<AuthorizationCodeMetadata> validate(@NotNull final Oauth2RegisteredClient client,
                                                            @NotNull final String authorizationCode,
                                                            @NotNull final AuthorizationCodeMetadata codeMetadata) {
        if ( codeMetadata.getGrantedFor().isSame(client) ) {
            System.out.println("validation successful");
            return Mono.just(codeMetadata);
        }
        return Mono.defer(() -> Mono.error(new AuthorizationCodeStolenException(authorizationCode)));
    }

    @NotNull
    private static DefaultAccessTokenResponse createResponse(@NotNull final Oauth2AccessToken token) {
        return DefaultAccessTokenResponse.authorizationCode()
                .accessTokenValue(token.getTokenValue())
                .expiresIn((int) token.remainingLifetime().getSeconds())
                .tokenType(token.getTokenType().typeName())
                .scopes(token.getScopes())
                .build();
    }

    @NotNull
    private static Oauth2AccessTokenGenerationContext createAccessTokenGenerationContext(@NotNull final AuthorizationCodeMetadata metadata) {
        return Oauth2AccessTokenGenerationContext.builder()
                .scopes(metadata.getRequestedScopes())
                .client(metadata.getGrantedFor())
                .resourceOwner(metadata.getGrantedBy())
                .build();
    }
}
