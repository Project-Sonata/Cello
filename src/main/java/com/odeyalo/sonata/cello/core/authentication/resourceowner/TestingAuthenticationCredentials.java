package com.odeyalo.sonata.cello.core.authentication.resourceowner;

/**
 * Primary used for tests
 */
public final class TestingAuthenticationCredentials implements AuthenticationCredentials {

    public static TestingAuthenticationCredentials create() {
        return new TestingAuthenticationCredentials();
    }
}
