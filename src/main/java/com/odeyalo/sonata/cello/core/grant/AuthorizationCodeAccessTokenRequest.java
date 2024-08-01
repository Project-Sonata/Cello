package com.odeyalo.sonata.cello.core.grant;

import com.odeyalo.sonata.cello.core.RedirectUri;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * {@link AccessTokenRequest} that used to create an access token from authorization code,
 * as described in <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3">Access token request</a>
 */
@Value
@Builder
public class AuthorizationCodeAccessTokenRequest implements AccessTokenRequest {
    /**
     * REQUIRED.  The authorization code received from the
     * authorization server.
     */
    @NotNull
    String authorizationCode;
    /**
     * REQUIRED, if the "redirect_uri" parameter was included in the
     * authorization request as described in Section 4.1.1, and their
     * values MUST be identical.
     */
    @NotNull
    RedirectUri redirectUri;
}
