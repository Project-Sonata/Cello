package com.odeyalo.sonata.cello.core.client.authentication;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * A strategy to load the {@link Oauth2RegisteredClient} from the given {@link ServerWebExchange}
 */
public interface Oauth2ClientResolverStrategy {

    /**
     * Load the {@link Oauth2RegisteredClient} from the {@link ServerWebExchange},
     * check the CREDENTIALS of the client
     * @param exchange - current http exchange
     * @return - a {@link Mono} with found client, empty {@link Mono} if implementation does not support it,
     * or {@link Mono} with an error if client has invalid credentials or client does not exist
     *
     * @throws com.odeyalo.sonata.cello.exception.InvalidClientCredentialsException - if client has invalid credentials
     */
    @NotNull
    Mono<Oauth2RegisteredClient> resolveClient(@NotNull ServerWebExchange exchange);

}
