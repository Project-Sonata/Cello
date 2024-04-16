package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.UriUtils;

import java.net.URI;
import java.util.Map;
import java.util.function.Supplier;

import static testing.UriAssert.assertThat;

class DefaultOauth2ProviderRedirectUriProviderManagerTest {
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
    void shouldReturnEmptyMonoIfNotSupportedProviderRequested() {
        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                new InMemoryOauth2ProviderRegistrationRepository()
        );

        testable.getProviderRedirectUri("google")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldRedirectToProvidedProviderUri() {

        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        URI result = testable.getProviderRedirectUri("google").block();

        assertThat(result).isEqualToWithoutQueryParameters(GOOGLE_PROVIDER_REGISTRATION.getProviderUri());
    }

    @Test
    void shouldReturnRedirectUriWithRedirectUriFromProvider() {
        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        URI result = testable.getProviderRedirectUri("google").block();

        assertThat(result).hasParameter("redirect_uri", GOOGLE_PROVIDER_REGISTRATION.getRedirectUri());
    }

    @Test
    void shouldReturnRedirectUriWithClientId() {
        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        URI result = testable.getProviderRedirectUri("google").block();

        assertThat(result).hasParameter("client_id", GOOGLE_PROVIDER_REGISTRATION.getClientId());
    }

    @Test
    void shouldReturnRedirectUriWithState() {
        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        URI result = testable.getProviderRedirectUri("google").block();

        assertThat(result).hasParameter("state");
    }

    @Test
    void shouldReturnRedirectUriWithScopes() {

        DefaultOauth2ProviderRedirectUriProviderManager testable = new DefaultOauth2ProviderRedirectUriProviderManager(
                googleOauth2ProviderRegistrationRepositorySupplier.get()
        );

        URI result = testable.getProviderRedirectUri("google").block();

        //noinspection DataFlowIssue
        String scopes = UriUtils.parseQueryParameters(result).get("scope");

        String[] parsedScopes = scopes.split(" ");
        Assertions.assertThat(parsedScopes).hasSize(2);
        Assertions.assertThat(parsedScopes).containsExactlyInAnyOrder("read", "write");
    }
}