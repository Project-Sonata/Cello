package com.odeyalo.sonata.cello.core.authentication.resourceowner;

/**
 * Default implementation of {@link ResourceOwnerInfo} that contains nothing
 */
public final class EmptyResourceOwnerInfo implements ResourceOwnerInfo {
    private static final EmptyResourceOwnerInfo INSTANCE = new EmptyResourceOwnerInfo();

    private EmptyResourceOwnerInfo() {}

    public static EmptyResourceOwnerInfo instance() {
        return INSTANCE;
    }
}
