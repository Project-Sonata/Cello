package com.odeyalo.sonata.cello.core.authentication.oauth2;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FOUND;

class Oauth2AuthenticationManagerTest {
    static final String STATE_VALUE = "123";
    static final URI REDIRECT_URI = URI.create("https://accounts.google.com/oauth2/login?client_id=odeyalo&state=123&scope=read%20write&redirect_uri=http:localhost:3000");

    @Test
    void shouldCompleteSuccessfully() {
        // given
        final Oauth2AuthenticationManager testable = new Oauth2AuthenticationManager(
                TestingOauth2StateGenerator.withState(STATE_VALUE),
                new TestingOauth2ProviderRedirectUriGenerator(),
                new InMemoryOauth2AuthenticationMetadataRepository()
        );
        final ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/login/google").build()
        );
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "FLOW_123");

        // when
        testable.startOauth2Authentication("google", exchange)
                .as(StepVerifier::create)
                // then
                .verifyComplete();
    }

    @Test
    void shouldReturnResponseWithRedirectStatus() {
        // given
        final Oauth2AuthenticationManager testable = new Oauth2AuthenticationManager(
                TestingOauth2StateGenerator.withState(STATE_VALUE),
                new TestingOauth2ProviderRedirectUriGenerator(),
                new InMemoryOauth2AuthenticationMetadataRepository()
        );
        final ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/login/google").build()
        );
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "FLOW_123");

        // when
        testable.startOauth2Authentication("google", exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        // then
        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(FOUND);
    }

    @Test
    void shouldReturnResponseWithLocationUriThatWasGeneratedByProvider() {
        // given
        final Oauth2AuthenticationManager testable = new Oauth2AuthenticationManager(
                TestingOauth2StateGenerator.withState(STATE_VALUE),
                new TestingOauth2ProviderRedirectUriGenerator(),
                new InMemoryOauth2AuthenticationMetadataRepository()
        );
        final ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/login/google").build()
        );
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "FLOW_123");

        // when
        testable.startOauth2Authentication("google", exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        // then
        assertThat(exchange.getResponse().getHeaders().getLocation()).isEqualTo(REDIRECT_URI);
    }

    @Test
    void authenticationMetadataMustBeSavedToMetadataRepository() {
        // given
        final var metadataRepository = new InMemoryOauth2AuthenticationMetadataRepository();
        final Oauth2AuthenticationManager testable = new Oauth2AuthenticationManager(
                TestingOauth2StateGenerator.withState(STATE_VALUE),
                new TestingOauth2ProviderRedirectUriGenerator(),
                metadataRepository
        );
        final ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/login/google").build()
        );
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "FLOW_123");

        // when
        testable.startOauth2Authentication("google", exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        // then
        metadataRepository.findBy(STATE_VALUE)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    void savedAuthenticationMetadataMustContainFlowIdEqualToProvidedFromAttributes() {
        // given
        final var metadataRepository = new InMemoryOauth2AuthenticationMetadataRepository();
        final Oauth2AuthenticationManager testable = new Oauth2AuthenticationManager(
                TestingOauth2StateGenerator.withState(STATE_VALUE),
                new TestingOauth2ProviderRedirectUriGenerator(),
                metadataRepository
        );

        final ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/oauth2/login/google").build()
        );
        exchange.getAttributes().put(Oauth2AuthorizationRequestRepository.CURRENT_FLOW_ATTRIBUTE_NAME, "FLOW_123");

        // when
        testable.startOauth2Authentication("google", exchange)
                .as(StepVerifier::create)
                .verifyComplete();

        // then
        metadataRepository.findBy(STATE_VALUE)
                .as(StepVerifier::create)
                .expectNextMatches(it -> Objects.equals(it.getFlowId(), "FLOW_123"))
                .verifyComplete();
    }

    private final static class TestingOauth2StateGenerator implements Oauth2StateGenerator {
        private final String state;

        public TestingOauth2StateGenerator(String state) {
            this.state = state;
        }

        public static TestingOauth2StateGenerator withState(String state) {
            return new TestingOauth2StateGenerator(state);
        }

        @Override
        public @NotNull Mono<String> generateState() {
            return Mono.just(state);
        }
    }
    private final static class TestingOauth2ProviderRedirectUriGenerator implements Oauth2ProviderRedirectUriGenerator {

        @Override
        @NotNull
        public Mono<URI> generateOauth2RedirectUri(@NotNull String providerName,
                                                   @NotNull Oauth2AuthenticationRedirectUriGenerationContext context) {
            return Mono.just(
                    REDIRECT_URI
            );
        }
    }
}