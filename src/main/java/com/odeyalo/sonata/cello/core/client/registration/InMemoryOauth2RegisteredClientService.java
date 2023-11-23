package com.odeyalo.sonata.cello.core.client.registration;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * In memory implementation that stores values in Map
 */
public final class InMemoryOauth2RegisteredClientService implements Oauth2RegisteredClientService {
    private final Map<String, Oauth2RegisteredClient> clients;

    public InMemoryOauth2RegisteredClientService() {
        this.clients = Collections.emptyMap();
    }

    public InMemoryOauth2RegisteredClientService(Oauth2RegisteredClient singleClient) {
        Assert.notNull(singleClient, "Client cannot be null!");
        this.clients = Collections.singletonMap(singleClient.getCredentials().getClientId(), singleClient);
    }

    public InMemoryOauth2RegisteredClientService(Collection<Oauth2RegisteredClient> clients) {
        this.clients = clients.stream()
                .collect(Collectors.toMap(
                        client -> client.getCredentials().getClientId(),
                        Function.identity()
                ));
    }

    @Override
    @NotNull
    public Mono<Oauth2RegisteredClient> findByClientId(@NotNull String clientId) {
        return Mono.justOrEmpty(
                clients.get(clientId)
        );
    }
}
