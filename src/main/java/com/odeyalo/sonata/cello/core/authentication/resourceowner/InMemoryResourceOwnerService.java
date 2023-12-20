package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InMemoryResourceOwnerService implements ResourceOwnerService {
    private final Map<String, ResourceOwner> cache;

    public InMemoryResourceOwnerService() {
        this.cache = Collections.emptyMap();
    }

    public InMemoryResourceOwnerService(Map<String, ResourceOwner> cache) {
        this.cache = cache;
    }

    public InMemoryResourceOwnerService(Collection<ResourceOwner> cache) {
        this.cache = cache.stream()
                .collect(Collectors.toMap(ResourceOwner::getPrincipal, Function.identity()));
    }

    @Override
    @NotNull
    public Mono<ResourceOwner> loadResourceOwnerByUsername(@NotNull String username) {
        return Mono.justOrEmpty(cache.get(username));
    }
}
