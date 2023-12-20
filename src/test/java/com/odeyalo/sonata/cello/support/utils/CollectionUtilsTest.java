package com.odeyalo.sonata.cello.support.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionUtilsTest {

    @Test
    void shouldReturnFalseIfNotOutOfBounds() {
        List<Integer> integers = List.of(1, 2, 3);

        boolean result = CollectionUtils.isOutOfBounds(integers, 2);

        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseIfIndexOnBoundOfCollection() {
        List<Integer> integers = List.of(1, 2, 3);

        boolean result = CollectionUtils.isOutOfBounds(integers, 2);

        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueIfOutOfBounds() {
        List<Integer> integers = List.of(1, 2, 3);

        boolean result = CollectionUtils.isOutOfBounds(integers, 10);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueIfIndexIsNegative() {
        List<Integer> integers = List.of(1, 2, 3);

        boolean result = CollectionUtils.isOutOfBounds(integers, -1);

        assertThat(result).isTrue();
    }
}