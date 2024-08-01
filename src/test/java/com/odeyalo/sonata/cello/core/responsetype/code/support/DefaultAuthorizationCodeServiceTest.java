package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.InMemoryAuthorizationCodeRepository;
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

class DefaultAuthorizationCodeServiceTest {

    @Test
    void shouldReturnRandomAuthorizationCode() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());

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
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());

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
                .assertNext(generatedCode -> assertThat(generatedCode.getMetadata().getRequestedScopes()).containsExactlyInAnyOrder(
                        SimpleScope.withName("read"),
                        SimpleScope.withName("write"))
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeWithTheClientForWhichAuthorizationHasBeenGranted() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());
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
                .assertNext(code -> assertThat(code.getMetadata().getGrantedFor()).isEqualTo(grantedFor))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeWithTheResourceOwnerThatGrantedAuthorization() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());
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
                .assertNext(code -> assertThat(code.getMetadata().getGrantedBy()).isEqualTo(grantedBy))
                .verifyComplete();
    }

    @Test
    void shouldReturnAuthorizationCodeGeneratedBySupplier() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(
                () -> "mocked_value",
                new InMemoryAuthorizationCodeRepository());

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

    @Test
    void shouldLoadMetadataAboutAuthorizationCodeIfAuthorizationCodeIsValid() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());

        final AuthorizationCodeGenerationContext generationContext = AuthorizationCodeGenerationContext.builder()
                .requestedScopes(ScopeContainer.fromArray(
                        SimpleScope.withName("write"),
                        SimpleScope.withName("read")
                ))
                .grantedBy(ResourceOwner.withPrincipalOnly("odeyalo"))
                .grantedFor(Oauth2RegisteredClientFaker.create().get())
                .build();

        final GeneratedAuthorizationCode generatedAuthorizationCode = testable.newAuthorizationCode(generationContext).block();

        assertThat(generatedAuthorizationCode).isNotNull();
        
        testable.loadUsing(generatedAuthorizationCode.getCodeValue())
                .as(StepVerifier::create)
                .assertNext(codeMetadata -> assertThat(codeMetadata).isEqualTo(generatedAuthorizationCode.getMetadata()))
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfAuthorizationCodeDoesNotExist() {
        final DefaultAuthorizationCodeService testable = new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());

        testable.loadUsing("not_existing")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}