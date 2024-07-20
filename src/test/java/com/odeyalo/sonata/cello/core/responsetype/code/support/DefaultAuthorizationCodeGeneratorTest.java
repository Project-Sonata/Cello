package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeGenerator.AuthorizationCodeGenerationContext;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultAuthorizationCodeGeneratorTest {

    @Test
    void shouldReturnRandomAuthorizationCode() {
        final DefaultAuthorizationCodeGenerator testable = new DefaultAuthorizationCodeGenerator();

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.singleScope(
                        SimpleScope.withName("read")
                ))
                .grantedBy(ResourceOwner.withPrincipalOnly("odeyalo"))
                .grantedFor(Oauth2RegisteredClientFaker.create().get())
                .build();

        final HashSet<GeneratedAuthorizationCode> generatedCodes = Flux.range(0, 3)
                .flatMap(it -> testable.newAuthorizationCode(generationContext))
                .collectList()
                .map(Sets::newHashSet)
                .block();

        assertThat(generatedCodes).hasSize(3);
    }

    @Test
    void shouldReturnAuthorizationCodeWithTheSameScopesAsWasProvided() {
        final DefaultAuthorizationCodeGenerator testable = new DefaultAuthorizationCodeGenerator();

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("write"),
                        SimpleScope.withName("read")
                ))
                .grantedBy(ResourceOwner.withPrincipalOnly("odeyalo"))
                .grantedFor(Oauth2RegisteredClientFaker.create().get())
                .build();

        testable.newAuthorizationCode(generationContext)
                .as(StepVerifier::create)
                .assertNext(it -> assertThat(it.getScopes()).containsExactlyInAnyOrder(
                        SimpleScope.withName("read"),
                        SimpleScope.withName("write"))
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeWithTheClientForWhichAuthorizationHasBeenGranted() {
        final DefaultAuthorizationCodeGenerator testable = new DefaultAuthorizationCodeGenerator();
        final Oauth2RegisteredClient grantedFor = Oauth2RegisteredClientFaker.create().get();

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("write"),
                        SimpleScope.withName("read")
                ))
                .grantedBy(ResourceOwner.withPrincipalOnly("odeyalo"))
                .grantedFor(grantedFor)
                .build();

        testable.newAuthorizationCode(generationContext)
                .as(StepVerifier::create)
                .assertNext(code -> assertThat(code.getGrantedFor()).isEqualTo(grantedFor))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeWithTheResourceOwnerThatGrantedAuthorization() {
        final DefaultAuthorizationCodeGenerator testable = new DefaultAuthorizationCodeGenerator();
        final ResourceOwner grantedBy = ResourceOwner.withPrincipalOnly("odeyalo");

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("write"),
                        SimpleScope.withName("read")
                ))
                .grantedBy(grantedBy)
                .grantedFor(Oauth2RegisteredClientFaker.create().get())
                .build();

        testable.newAuthorizationCode(generationContext)
                .as(StepVerifier::create)
                .assertNext(code -> assertThat(code.getGrantedBy()).isEqualTo(grantedBy))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeGeneratedBySupplier() {
        final DefaultAuthorizationCodeGenerator testable = new DefaultAuthorizationCodeGenerator(
                () -> "mocked_value"
        );

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("write"),
                        SimpleScope.withName("read")
                ))
                .grantedBy(ResourceOwner.withPrincipalOnly("odeyalo"))
                .grantedFor(Oauth2RegisteredClientFaker.create().get())
                .build();

        testable.newAuthorizationCode(generationContext)
                .as(StepVerifier::create)
                .assertNext(code -> assertThat(code.getCodeValue()).isEqualTo("mocked_value"))
                .verifyComplete();
    }
}