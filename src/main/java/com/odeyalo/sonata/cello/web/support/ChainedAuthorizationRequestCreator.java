package com.odeyalo.sonata.cello.web.support;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Create the {@link AuthorizationRequest} from list of {@link AuthorizationRequestCreationStage}
 */
@Component
public class ChainedAuthorizationRequestCreator implements AuthorizationRequestCreator {
    private final List<AuthorizationRequestCreationStage> chain;

    public ChainedAuthorizationRequestCreator(List<AuthorizationRequestCreationStage> chain) {
        this.chain = chain;
    }

    @Override
    @NotNull
    public Mono<AuthorizationRequest> createAuthorizationRequest(@NotNull ServerWebExchange exchange) {
        AuthorizationRequest.AuthorizationRequestBuilder emptyBuilder = AuthorizationRequest.builder();
        return Flux.fromIterable(chain)
                .flatMap(stage -> stage.processCreation(exchange, emptyBuilder))
                .reduce((intermediate, current) -> current)
                .map(AuthorizationRequest.AuthorizationRequestBuilder::build);
    }
}
