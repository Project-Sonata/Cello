package com.odeyalo.sonata.cello.core.client.authentication;

import com.odeyalo.sonata.cello.core.client.ClientType;
import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import com.odeyalo.sonata.cello.exception.InvalidClientCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

class FormDataOauth2ClientResolverStrategyTest {

    @Test
    void shouldLoadClientIfProvidedCredentialsAreValid() {
        final Oauth2RegisteredClient client = Oauth2RegisteredClientFaker.create()
                .clientType(ClientType.CONFIDENTIAL)
                .credentials(Oauth2ClientCredentials.of("miku", "secret_password"))
                .get();

        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService(client));

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=miku&client_secret=secret_password")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .expectNext(client)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfClientByProvidedClientIdWasNotFound() {
        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService());

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=miku&client_secret=secret_password")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .verifyError(InvalidClientCredentialsException.class);
    }

    @Test
    void shouldReturnErrorIfInvalidCredentialsWereProvided() {
        final Oauth2RegisteredClient client = Oauth2RegisteredClientFaker.create()
                .clientType(ClientType.CONFIDENTIAL)
                .credentials(Oauth2ClientCredentials.of("miku", "s3cr3t_password"))
                .get();

        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService(client));

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=miku&client_secret=secret_password")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .verifyError(InvalidClientCredentialsException.class);
    }

    @Test
    void shouldReturnNothingIfClientIdWasNotProvided() {
        final Oauth2RegisteredClient client = Oauth2RegisteredClientFaker.create()
                .clientType(ClientType.CONFIDENTIAL)
                .credentials(Oauth2ClientCredentials.of("miku", "s3cr3t_password"))
                .get();

        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService(client));

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_secret=secret_password")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorIfClientSecretWasNotProvidedButClientIdWasProvidedAndClientIsConfidential() {
        final Oauth2RegisteredClient client = Oauth2RegisteredClientFaker.create()
                .clientType(ClientType.CONFIDENTIAL)
                .credentials(Oauth2ClientCredentials.of("miku", "s3cr3t_password"))
                .get();

        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService(client));

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=miku")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .verifyError(InvalidClientCredentialsException.class);
    }

    @Test
    void shouldReturnClientIfClientSecretWasNotSuppliedButClientTypeIsPublic() {
        final Oauth2RegisteredClient client = Oauth2RegisteredClientFaker.create()
                .clientType(ClientType.PUBLIC)
                .credentials(Oauth2ClientCredentials.withId("miku"))
                .get();

        final var testable = new FormDataOauth2ClientResolverStrategy(new InMemoryOauth2RegisteredClientService(client));

        final ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("client_id=miku")
        );

        testable.resolveClient(exchange)
                .as(StepVerifier::create)
                .expectNext(client)
                .verifyComplete();
    }
}