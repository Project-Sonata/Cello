package com.odeyalo.sonata.cello.core.token.access;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

class InMemoryAccessTokenStoreTest {

    @Test
    void shouldCompletedSuccessfullyOnSaved() {
        InMemoryAccessTokenStore testable = new InMemoryAccessTokenStore();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue("hello")
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();

        testable.saveToken(token)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldSaveTheToken() {
        InMemoryAccessTokenStore testable = new InMemoryAccessTokenStore();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue("hello")
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();

        testable.saveToken(token)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findTokenByTokenValue(token.getTokenValue())
                .as(StepVerifier::create)
                .expectNext(token)
                .verifyComplete();
    }

    @Test
    void shouldReturnNothingIfNotExistByTokenValue() {
        InMemoryAccessTokenStore testable = new InMemoryAccessTokenStore();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue("hello")
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();

        testable.saveToken(token)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.findTokenByTokenValue("not_exist")
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueIfTokenExistByValue() {
        InMemoryAccessTokenStore testable = new InMemoryAccessTokenStore();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue("hello")
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();

        testable.saveToken(token)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.exists(token.getTokenValue())
                .as(StepVerifier::create)
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseIfNotExist() {
        InMemoryAccessTokenStore testable = new InMemoryAccessTokenStore();

        Oauth2AccessToken token = Oauth2AccessToken.builder()
                .tokenValue("hello")
                .issuedAt(Instant.now())
                .expiresIn(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();

        testable.saveToken(token)
                .as(StepVerifier::create)
                .verifyComplete();

        testable.exists("not_exist")
                .as(StepVerifier::create)
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }
}