package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeClaims;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeMetadata;
import com.odeyalo.sonata.cello.core.token.access.InMemoryAccessTokenStore;
import com.odeyalo.sonata.cello.core.token.access.MockOauth2AccessTokenGenerator;
import com.odeyalo.sonata.cello.core.token.access.OpaquePersistentOauth2AccessTokenGenerator;
import com.odeyalo.sonata.cello.exception.AuthorizationCodeStolenException;
import com.odeyalo.sonata.cello.exception.InvalidAuthorizationCodeException;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationCodeGrantHandlerTest {

    @Test
    void shouldGenerateAccessTokenIfAuthorizationCodeIsValid() {
        // given
        final Oauth2RegisteredClient oauth2Client = Oauth2RegisteredClientFaker.create().get();
        final AuthorizationCodeGrantHandler testable = new AuthorizationCodeGrantHandler(
                (code) -> Mono.just(AuthorizationCodeMetadata.from(
                        ResourceOwner.withPrincipalOnly("odeyalo"),
                        oauth2Client,
                        ScopeContainer.singleScope(SimpleScope.withName("read")),
                        AuthorizationCodeClaims.empty()
                )),
                new OpaquePersistentOauth2AccessTokenGenerator(new InMemoryAccessTokenStore(), () -> Mono.just("mocked"))
        );

        final var request = new AuthorizationCodeAccessTokenRequest("123", RedirectUri.create("http://localhost:3000/callback"));

        // when
        testable.handle(request, oauth2Client)
                .as(StepVerifier::create)
                // then
                .assertNext(accessToken -> {
                    assertThat(accessToken.getAccessTokenValue()).isEqualTo("mocked");
                    assertThat(accessToken.getTokenType()).isEqualTo("Bearer");
                    assertThat(accessToken.getExpiresIn()).isCloseTo(3600, Percentage.withPercentage(1.0));
                    assertThat(accessToken.getScopes()).containsExactlyInAnyOrder(SimpleScope.withName("read"));
                    assertThat(accessToken.getRefreshToken()).isNull();
                    assertThat(accessToken.getGrantType()).isEqualTo(GrantType.AUTHORIZATION_CODE);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfAuthorizationCodeDoesNotExist() {
        // given
        final Oauth2RegisteredClient oauth2Client = Oauth2RegisteredClientFaker.create().get();
        final AuthorizationCodeGrantHandler testable = new AuthorizationCodeGrantHandler(
                (code) -> Mono.empty(),
                new MockOauth2AccessTokenGenerator("mocked")
        );

        final var request = new AuthorizationCodeAccessTokenRequest("123", RedirectUri.create("http://localhost:3000/callback"));
        // when
        testable.handle(request, oauth2Client)
                .as(StepVerifier::create)
                // then
                .verifyError(InvalidAuthorizationCodeException.class);
    }

    @Test
    void shouldReturnErrorIfClientIsDifferentFromTheOneWhoRequestAuthorizationCode() {
        final AuthorizationCodeGrantHandler testable = new AuthorizationCodeGrantHandler(
                (code) -> Mono.just(AuthorizationCodeMetadata.from(
                        ResourceOwner.withPrincipalOnly("odeyalo"),
                        Oauth2RegisteredClientFaker.create().get(),
                        ScopeContainer.singleScope(SimpleScope.withName("read")),
                        AuthorizationCodeClaims.empty()
                )),
                new OpaquePersistentOauth2AccessTokenGenerator(new InMemoryAccessTokenStore(), () -> Mono.just("mocked"))
        );
        final var request = new AuthorizationCodeAccessTokenRequest("123", RedirectUri.create("http://localhost:3000/callback"));

        final Oauth2RegisteredClient differentClient = Oauth2RegisteredClientFaker.create().get();


        testable.handle(request, differentClient)
                .as(StepVerifier::create)
                .verifyError(AuthorizationCodeStolenException.class);
    }
}