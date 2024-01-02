package com.odeyalo.sonata.cello.web;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filter to load the authentication from {@link ServerSecurityContextRepository} if present,
 * otherwise the {@link WebFilterChain} is continued as default
 */
@Component
public class AuthenticationLoaderFilter implements WebFilter {
    private final ServerSecurityContextRepository securityContextRepository;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationLoaderFilter.class);

    public AuthenticationLoaderFilter(ServerSecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull ServerWebExchange exchange,
                             @NotNull WebFilterChain chain) {

        return securityContextRepository.load(exchange)
                .map(securityContext -> ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                .doOnNext(context -> logger.info("Successfully set the {} from the {}", context, securityContextRepository))
                .then(chain.filter(exchange));
    }
}
