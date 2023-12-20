package com.odeyalo.sonata.cello.core;

import com.odeyalo.sonata.cello.support.utils.CollectionUtils;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Immutable container for scopes
 */
@Value
@Builder
public class ScopeContainer implements Iterable<Scope> {
    @Singular
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
}
