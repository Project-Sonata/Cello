package com.odeyalo.sonata.cello.core;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable implementation of {@link Scope} that just set the name of the scope
 */
@Value
@AllArgsConstructor(staticName = "withName")
public class SimpleScope implements Scope {
    @NotNull
    @NonNull
    String name;

    @Override
    @NotNull
    public String getName() {
        return name;
    }
}
