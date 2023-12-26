package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Indicates that the {@link com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest} is invalid and cannot be used to access resource owner authorization
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class Oauth2AuthorizationRequestValidationException extends RuntimeException {
    /**
     * REQUIRED.  A single ASCII [USASCII] error code from the
     * following enumeration {@link Oauth2ErrorCode}.
     */
    @NotNull
    Oauth2ErrorCode error;
    /**
     * error_description
     * OPTIONAL.  Human-readable ASCII [USASCII] text providing
     * additional information, used to assist the client developer in
     * understanding the error that occurred.
     * Values for the "error_description" parameter MUST NOT include
     * characters outside the set %x20-21 / %x23-5B / %x5D-7E.
     */
    @Nullable
    String description;
    /**
     * error_uri
     * OPTIONAL.  A URI identifying a human-readable web page with
     * information about the error, used to provide the client
     * developer with additional information about the error.
     * Values for the "error_uri" parameter MUST conform to the
     * URI-reference syntax and thus MUST NOT include characters
     * outside the set %x21 / %x23-5B / %x5D-7E.
     */
    @Nullable
    String errorUri;
    /**
     * REQUIRED if a "state" parameter was present in the client
     * authorization request.  The exact value received from the
     * client.
     */
    @Nullable
    String state;

    public static Oauth2AuthorizationRequestValidationException errorCodeOnly(@NotNull Oauth2ErrorCode error) {
        return builder().error(error).build();
    }
}
