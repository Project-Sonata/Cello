package com.odeyalo.sonata.cello.core;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

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
    List<Scope> items;

    public static ScopeContainer single(Scope scope) {
        Assert.notNull(scope, "Scope cannot be null!");
        return builder().item(scope).build();
    }

    public static ScopeContainer fromItems(Scope... scopes) {
        return fromCollection(List.of(scopes));
    }

    public static ScopeContainer fromCollection(Collection<? extends Scope> scopes) {
        Assert.notNull(scopes, "Scopes cannot be null!");
        Assert.noNullElements(scopes, "Collection cannot contain null value!");
        return builder().items(scopes).build();
    }

    public int size() {
        return getItems().size();
    }

    public boolean containsAll(@NotNull Collection<Scope> c) {
        return new HashSet<>(getItems()).containsAll(c); // wrap in HashSet for optimization purposes
    }

    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    public boolean contains(Scope o) {
        return getItems().contains(o);
    }

    public Scope get(int index) {
        return getItems().get(index);
    }

    @NotNull
    @Override
    public Iterator<Scope> iterator() {
        return null;
    }
}
