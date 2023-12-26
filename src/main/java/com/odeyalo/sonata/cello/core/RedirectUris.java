package com.odeyalo.sonata.cello.core;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represent the immutable collection with {@link RedirectUri}.
 * Note, that this collection allow the duplication of elements.
 */
@Value
@Builder
public class RedirectUris implements Iterable<RedirectUri> {
    @NotNull
    @Singular("redirectUri")
    @Getter(value = AccessLevel.PRIVATE)
    List<RedirectUri> redirectUris;

    public static RedirectUris empty() {
        return builder().build();
    }

    public static RedirectUris single(RedirectUri redirectUri) {
        return builder().redirectUri(redirectUri).build();
    }

    public static RedirectUris fromCollection(Collection<RedirectUri> uris) {
        return builder().redirectUris(uris).build();
    }

    public static RedirectUris fromArray(RedirectUri... uris) {
        return builder().redirectUris(List.of(uris)).build();
    }

    public int size() {
        return getRedirectUris().size();
    }

    public boolean isEmpty() {
        return getRedirectUris().isEmpty();
    }

    public boolean contains(RedirectUri o) {
        return getRedirectUris().contains(o);
    }

    public RedirectUri get(int index) {
        return getRedirectUris().get(index);
    }

    @NotNull
    @Override
    public Iterator<RedirectUri> iterator() {
        return redirectUris.iterator();
    }
}
