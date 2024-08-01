package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Factory to create an instance of class that wraps the claims represented as Key-Value pair
 * @param <T> - return type
 */
public interface ClaimsWrapperFactory<T> {
    /**
     * Create a new {@link T} from the given Map
     *
     * @param source - a map to create instance of {@link T} from
     * @return - created T
     * @throws IllegalStateException if the required key is missing or value is invalid
     */
    @NotNull
    T create(@NotNull Map<String, Object> source);
}
