package com.odeyalo.sonata.cello.core.token.access;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.ClientProfile;
import com.odeyalo.sonata.cello.core.client.ClientType;
import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class OpaquePersistentOauth2AccessTokenGeneratorTest {

    @Test
    void shouldReturnNotNullToken() {
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                new InMemoryAccessTokenStore(),
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(ScopeContainer.empty())
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        testable.generateToken(context)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void shouldSaveToStore() {
        InMemoryAccessTokenStore store = new InMemoryAccessTokenStore();
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                store,
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(ScopeContainer.empty())
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        Oauth2AccessToken generatedToken = testable.generateToken(context).block();

        store.findTokenByTokenValue(generatedToken.getTokenValue())
                .as(StepVerifier::create)
                .expectNext(generatedToken)
                .verifyComplete();
    }

    @Test
    void shouldReturnDifferentTokensEveryTime() {
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                new InMemoryAccessTokenStore(),
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(ScopeContainer.empty())
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        Oauth2AccessToken token1 = testable.generateToken(context).block();
        Oauth2AccessToken token2 = testable.generateToken(context).block();
        Oauth2AccessToken token3 = testable.generateToken(context).block();

        assertThat(token1).isNotEqualTo(token2);
        assertThat(token2).isNotEqualTo(token3);
    }

    @Test
    void shouldReturnTokenWithTimeWhenTokenWasIssued() {
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                new InMemoryAccessTokenStore(),
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(ScopeContainer.empty())
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        testable.generateToken(context)
                .as(StepVerifier::create)
                .expectNextMatches(token -> token.getIssuedAt() != null)
                .verifyComplete();
    }

    @Test
    void shouldReturnTokenWithTimeWhenTokenWillExpire() {
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                new InMemoryAccessTokenStore(),
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(ScopeContainer.empty())
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        testable.generateToken(context)
                .as(StepVerifier::create)
                .expectNextMatches(token -> token.getExpiresIn() != null)
                .verifyComplete();
    }

    @Test
    void shouldReturnRequestedScopesInToken() {
        OpaquePersistentOauth2AccessTokenGenerator testable = new OpaquePersistentOauth2AccessTokenGenerator(
                new InMemoryAccessTokenStore(),
                new UUIDOpaqueOauth2AccessTokenValueGenerator()
        );

        ScopeContainer scopes = ScopeContainer.fromArray(
                SimpleScope.withName("read"),
                SimpleScope.withName("write")
        );
        Oauth2AccessTokenGenerationContext context = Oauth2AccessTokenGenerationContext.builder()
                .client(
                        Oauth2RegisteredClient.builder()
                                .credentials(Oauth2ClientCredentials.withId("odeyaloooo"))
                                .clientType(ClientType.PUBLIC)
                                .clientProfile(ClientProfile.WEB_APPLICATION)
                                .build()
                )
                .scopes(scopes)
                .resourceOwner(ResourceOwner.withPrincipalOnly("odeyalo"))
                .build();

        testable.generateToken(context)
                .as(StepVerifier::create)
                .expectNextMatches(token -> {
                    assertThat(token.getScopes()).containsAll(scopes);
                    return true;
                })
                .verifyComplete();
    }
}