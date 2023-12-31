package com.odeyalo.sonata.cello.core.consent;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolve the {@link ConsentDecision} from the {@link ServerWebExchange} in any possible way
 */
public interface ConsentDecisionResolver {
    /**
     * Resolve the {@link ConsentDecision} from the given {@link ServerWebExchange}
     * @param exchange - current http exchange
     * @return - {@link Mono} populated with {@link ConsentDecision} if this implementation supports this type of resolving,
     * empty {@link Mono} if not supported by implementation. Should NOT throw exceptions. Default return value should be denied {@link ConsentDecision}
     */
    @NotNull
    Mono<ConsentDecision> resolveConsentDecision(@NotNull ServerWebExchange exchange);


}
