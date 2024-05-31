package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.support.utils.CollectionUtils;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

/**
 * Immutable container for scopes
 */
@Value
@Builder
public class ScopeContainer implements Iterable<Scope> {
    @Singular
    @Getter(value = AccessLevel.PRIVATE)
    List<Scope> scopes;

    public static ScopeContainer empty() {
        return builder().build();
    }

    public static ScopeContainer singleScope(Scope scope) {
        return builder().scope(scope).build();
    }

    public static ScopeContainer fromCollection(Collection<? extends Scope> scopes) {
        return builder().scopes(scopes).build();
    }

    public static ScopeContainer fromOauth2String(@Nullable String scopesInOauth2Spec) {
        if (scopesInOauth2Spec == null) {
            return empty();
        }

        List<SimpleScope> scopes = Arrays.stream(scopesInOauth2Spec.split(" "))
                .map(SimpleScope::withName)
                .toList();

        return builder().scopes(scopes).build();
    }

    public static ScopeContainer fromArray(Scope... scopes) {
        return builder().scopes(List.of(scopes)).build();
    }

    public int size() {
        return getScopes().size();
    }

    public boolean isEmpty() {
        return getScopes().isEmpty();
    }

    public boolean contains(Scope scope) {
        return getScopes().contains(scope);
    }

    public boolean containsAll(@NotNull Collection<Scope> scopes) {
        return new HashSet<>(getScopes()).containsAll(scopes);
    }

    public String asOauth2String() {
        return String.join(" ", stream().map(Scope::getName).toList());
    }

    @Nullable
    public Scope get(int index) {
        if ( CollectionUtils.isOutOfBounds(scopes, index) ) {
            return null;
        }
        return getScopes().get(index);
    }

    @NotNull
    @Override
    public Iterator<Scope> iterator() {
        return scopes.iterator();
    }

    public Stream<Scope> stream() {
        return getScopes().stream();
    }
}
