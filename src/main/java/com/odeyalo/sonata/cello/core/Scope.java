package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

/**
 * Represent the Oauth2 scope in Cello project.
 * Interface because to make it extendable and easy configurable.
 * Implementations can add description of scope, for example, or anything.
 *
 * Default implementation is {@link SimpleScope}
 */
public interface Scope {
    /**
     * @return string representation of this scope name
     */
    @NotNull
    String getName();

}
