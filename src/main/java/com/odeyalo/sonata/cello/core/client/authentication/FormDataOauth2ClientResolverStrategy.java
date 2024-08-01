package com.odeyalo.sonata.cello.core.client.authentication;

import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.exception.InvalidClientCredentialsException;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.CLIENT_ID;
import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.CLIENT_SECRET;

/**
 * Tries to load the {@link Oauth2RegisteredClient} from the form data parameters, returns empty {@link Mono} if 'client_id' in form data is missing
 */
public final class FormDataOauth2ClientResolverStrategy implements Oauth2ClientResolverStrategy {
    private final Oauth2RegisteredClientService registeredClientService;

    public FormDataOauth2ClientResolverStrategy(final Oauth2RegisteredClientService registeredClientService) {
        this.registeredClientService = registeredClientService;
    }


    @Override
    @NotNull
    public Mono<Oauth2RegisteredClient> resolveClient(@NotNull final ServerWebExchange exchange) {
        return exchange.getFormData()
                .flatMap(formData -> tryLoadOauth2Client(formData));
    }

    @NotNull
    private Mono<Oauth2RegisteredClient> tryLoadOauth2Client(@NotNull final MultiValueMap<String, String> formData) {
        final String clientId = formData.getFirst(CLIENT_ID);
        final String clientSecret = formData.getFirst(CLIENT_SECRET);

        if ( clientId == null ) {
            return Mono.empty();
        }

        return registeredClientService.findByClientId(clientId)
                .flatMap(client -> checkCredentials(client, Oauth2ClientCredentials.of(clientId, clientSecret)))
                .switchIfEmpty(Mono.defer(() -> invalidCredentialsError()));
    }

    @NotNull
    private static Mono<Oauth2RegisteredClient> checkCredentials(@NotNull final Oauth2RegisteredClient loadedClient,
                                                                 @NotNull final Oauth2ClientCredentials usedCredentials) {

        if ( loadedClient.checkCredentials(usedCredentials) ) {
            return Mono.just(loadedClient);
        }

        return Mono.defer(() -> invalidCredentialsError());
    }

    @NotNull
    private static Mono<Oauth2RegisteredClient> invalidCredentialsError() {
        return Mono.error(new InvalidClientCredentialsException(
                "Invalid credentials are used",
                AuthenticationStrategy.CLIENT_SECRET_POST
        ));
    }
}
