package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

/**
 * Represent ONLY Oauth2 supported error codes described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.2.2.1">Error Response</a>
 */
public enum Oauth2ErrorCode {
    /**
     * The request is missing a required parameter, includes an
     * invalid parameter value, includes a parameter more than
     * once, or is otherwise malformed.
     */
    INVALID_REQUEST("invalid_request"),
    /**
     * The client is not authorized to request an authorization
     * code using this method.
     */
    UNAUTHORIZED_CLIENT("unauthorized_client"),
    /**
     * Client authentication failed (e.g., unknown client, no
     * client authentication included, or unsupported
     * authentication method).  The authorization server MAY
     * return an HTTP 401 (Unauthorized) status code to indicate
     * which HTTP authentication schemes are supported.  If the
     * client attempted to authenticate via the "Authorization"
     * request header field, the authorization server MUST
     * respond with an HTTP 401 (Unauthorized) status code and
     * include the "WWW-Authenticate" response header field
     * matching the authentication scheme used by the client.
     */
    INVALID_CLIENT("invalid_client"),
    /**
     * The resource owner or authorization server denied the
     * request.
     */
    ACCESS_DENIED("access_denied"),
    /**
     * The authorization server does not support obtaining an
     * authorization code using this method.
     */
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type"),
    /**
     * The requested scope is invalid, unknown, or malformed.
     */
    INVALID_SCOPE("INVALID_SCOPE"),
    /**
     * The authorization server encountered an unexpected
     * condition that prevented it from fulfilling the request.
     * (This error code is needed because a 500 Internal Server
     * Error HTTP status code cannot be returned to the client
     * via an HTTP redirect.)
     */
    SERVER_ERROR("server_error"),
    /**
     * The authorization server is currently unable to handle
     * the request due to a temporary overloading or maintenance
     * of the server.  (This error code is needed because a 503
     * Service Unavailable HTTP status code cannot be returned
     * to the client via an HTTP redirect.)
     */
    TEMPORARILY_UNAVAILABLE("temporarily_unavailable");


    private String name;

    Oauth2ErrorCode(@NotNull String name) {
        this.name = name;
    }

    /**
     * Represent the {@link Oauth2ErrorCode} as {@link String} required by <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1">Error Response</a>
     *
     * @return - string representation of this error code
     */
    public String asSpecificationString() {
        return name;
    }
}
