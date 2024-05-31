package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Primary used for tests
 */
public final class TestingAuthenticatedResourceOwnerAuthentication extends AuthenticatedResourceOwnerAuthentication {

    public TestingAuthenticatedResourceOwnerAuthentication(@NotNull final String principal,
                                                           @Nullable Object credentails,
                                                           @NotNull final ResourceOwner resourceOwner) {
        super(principal, credentails, resourceOwner);
    }

    public static TestingAuthenticatedResourceOwnerAuthentication random() {
        return new TestingAuthenticatedResourceOwnerAuthentication(RandomStringUtils.randomAlphanumeric(22),
                RandomStringUtils.randomAlphanumeric(22),
                ResourceOwner.withPrincipalOnly("i am mocked user"));
    }
}
