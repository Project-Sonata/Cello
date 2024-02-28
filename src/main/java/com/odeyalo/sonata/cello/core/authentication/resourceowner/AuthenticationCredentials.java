package com.odeyalo.sonata.cello.core.authentication.resourceowner;

/**
 * Marker interface for any possible authentication credentials.
 * <p>
 * Marker interface because there is no standard for authentication schemas, while username and password schema requires only username password fields,
 * OIDC connect requires an OIDC token.
 * <p>
 * Despite the fact that marker interfaces are code smell, was decided to do it in this way in order to be able to use any possible authenticate schema
 */
public interface AuthenticationCredentials {
}
