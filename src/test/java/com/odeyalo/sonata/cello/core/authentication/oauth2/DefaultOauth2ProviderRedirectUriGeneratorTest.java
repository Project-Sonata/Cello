package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.exception.NotSupportedOauth2ProviderException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UriUtils;

import java.net.URI;
import java.util.Map;
import java.util.function.Supplier;

import static testing.UriAssert.assertThat;

class DefaultOauth2ProviderRedirectUriGeneratorTest {
    static final Oauth2ProviderRegistration GOOGLE_PROVIDER_REGISTRATION = Oauth2ProviderRegistration.builder()
            .providerUri("http://localhost:3000/oauth2")
            .redirectUri("http://localhost:4000/oauth2/login/google/callback")
            .scopes(ScopeContainer.fromArray(
                    SimpleScope.withName("read"),
                    SimpleScope.withName("write")
            ))
            .clientId("odeyalo")
            .clientSecret("s3cr3t")
            .build();

    final Supplier<Oauth2ProviderRegistrationRepository> googleOauth2ProviderRegistrationRepositorySupplier =
            () -> new InMemoryOauth2ProviderRegistrationRepository(Map.of("google", GOOGLE_PROVIDER_REGISTRATION));

    @Test
    void shouldRedirectToProvidedProviderUri() {

        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        assertThat(result).isEqualToWithoutQueryParameters(GOOGLE_PROVIDER_REGISTRATION.getProviderUri());
    }

    @Test
    void shouldReturnRedirectUriWithRedirectUriFromProvider() {
        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        assertThat(result).hasParameter("redirect_uri", GOOGLE_PROVIDER_REGISTRATION.getRedirectUri());
    }

    @Test
    void shouldReturnRedirectUriWithClientId() {
        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        assertThat(result).hasParameter("client_id", GOOGLE_PROVIDER_REGISTRATION.getClientId());
    }

    @Test
    void shouldReturnRedirectUriWithState() {
        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        assertThat(result).hasParameter("state");
    }

    @Test
    void shouldReturnRedirectUriWithStateThatEqualToProvided() {
        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        assertThat(result).hasParameter("state", "state_123");
    }

    @Test
    void shouldReturnRedirectUriWithScopes() {

        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        URI result = testable.generateOauth2RedirectUri("google", context).block();

        //noinspection DataFlowIssue
        String scopes = UriUtils.parseQueryParameters(result).get("scope");

        String[] parsedScopes = scopes.split(" ");
        Assertions.assertThat(parsedScopes).hasSize(2);
        Assertions.assertThat(parsedScopes).containsExactlyInAnyOrder("read", "write");
    }

    @Test
    void shouldReturnErrorIfProviderIsNotSupported() {
        DefaultOauth2ProviderRedirectUriGenerator testable = new DefaultOauth2ProviderRedirectUriGenerator(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        Oauth2AuthenticationRedirectUriGenerationContext context = Oauth2AuthenticationRedirectUriGenerationContext.builder()
                .withState("state_123")
                .build();

        testable.generateOauth2RedirectUri("unsupported", context)
                .as(StepVerifier::create)
                .expectError(NotSupportedOauth2ProviderException.class)
                .verify();
    }
}