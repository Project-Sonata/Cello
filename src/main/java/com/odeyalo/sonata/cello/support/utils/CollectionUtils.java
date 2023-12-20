package com.odeyalo.sonata.cello.support.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("Cannot be created using constructor");
    }


    public static <T> boolean isOutOfBounds(Collection<T> targetCollection, int index) {
        return targetCollection != null &&
                (index < 0 || targetCollection.size() < index);
    }
}
