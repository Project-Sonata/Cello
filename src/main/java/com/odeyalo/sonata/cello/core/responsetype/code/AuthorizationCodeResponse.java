package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2">Authorization code response</a>
 */
@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class AuthorizationCodeResponse implements Oauth2AuthorizationResponse<AuthorizationCodeRequest> {
    @NotNull
    String authorizationCode;
    @NotNull
    AuthorizationCodeRequest associatedRequest;

    @Override
    @NotNull
    public AuthorizationCodeRequest getAssociatedRequest() {
        return associatedRequest;
    }

    public static AuthorizationCodeResponse.AuthorizationCodeResponseBuilder withAssociatedRequest(final AuthorizationCodeRequest request) {
        return builder().associatedRequest(request);
    }

    private static AuthorizationCodeResponse.AuthorizationCodeResponseBuilder builder() {
        return new AuthorizationCodeResponse.AuthorizationCodeResponseBuilder();
    }
}
