package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeClaims;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeMetadata;
import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

import java.util.Map;

class InMemoryAuthorizationCodeRepositoryTest {

    public static final AuthorizationCodeMetadata SIMPLE_AUTHORIZATION_CODE_METADATA = AuthorizationCodeMetadata.from(
            ResourceOwner.withPrincipalOnly("odeyalo"),
            Oauth2RegisteredClientFaker.create().get(),
            ScopeContainer.singleScope(SimpleScope.withName("read")),
            AuthorizationCodeClaims.empty()
    );

    @Test
    void shouldCompleteSuccessfullyIfGeneratedCodeWasSaved() {
        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository();
        final GeneratedAuthorizationCode generatedAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue("123")
                .metadata(SIMPLE_AUTHORIZATION_CODE_METADATA)
                .build();

        testable.save(generatedAuthorizationCode)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldFoundAuthorizationCodeByItsCodeValueAfterSave() {
        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository();
        final GeneratedAuthorizationCode generatedAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue("123")
                .metadata(SIMPLE_AUTHORIZATION_CODE_METADATA)
                .build();

        testable.save(generatedAuthorizationCode)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findByCodeValue("123")
                .as(StepVerifier::create)
                .expectNext(generatedAuthorizationCode)
                .verifyComplete();
    }

    @Test
    void shouldFindAuthorizationCodeFromPredefinedCache() {
        final GeneratedAuthorizationCode existingAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue("123")
                .metadata(SIMPLE_AUTHORIZATION_CODE_METADATA)
                .build();

        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository(
                Map.of("123", existingAuthorizationCode)
        );

        testable.findByCodeValue("123")
                .as(StepVerifier::create)
                .expectNext(existingAuthorizationCode)
                .verifyComplete();
    }

    @Test
    void shouldDeleteTheAuthorizationCodeByItsValue() {
        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository();
        final GeneratedAuthorizationCode generatedAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue("123")
                .metadata(SIMPLE_AUTHORIZATION_CODE_METADATA)
                .build();

        testable.save(generatedAuthorizationCode)
                .as(StepVerifier::create)
                .verifyComplete();
        // when
        testable.deleteByCodeValue("123")
                .as(StepVerifier::create)
                .verifyComplete();
        // then
        testable.findByCodeValue("123")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldDeleteNothingIfAuthorizationCodeValueIsNotAssociatedWithAnyAuthorizationCode() {
        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository();
        final GeneratedAuthorizationCode generatedAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue("123")
                .metadata(SIMPLE_AUTHORIZATION_CODE_METADATA)
                .build();

        testable.save(generatedAuthorizationCode)
                .as(StepVerifier::create)
                .verifyComplete();
        // when
        testable.deleteByCodeValue("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
        // then
        testable.findByCodeValue("123")
                .as(StepVerifier::create)
                .expectNext(generatedAuthorizationCode)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfAuthorizationCodeDoesNotExist() {
        final InMemoryAuthorizationCodeRepository testable = new InMemoryAuthorizationCodeRepository();

        testable.findByCodeValue("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }
}