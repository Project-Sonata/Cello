package com.odeyalo.sonata.cello.core.grant;

import lombok.Value;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Value
public class DefaultAccessTokenResponse extends AbstractAccessTokenResponse {

    private DefaultAccessTokenResponse(final AbstractAccessTokenResponseBuilder<?, ?> b) {
        super(b);
    }

    public static DefaultAccessTokenResponse.DefaultAccessTokenResponseBuilder<?, ?> authorizationCode() {
        return builder().grantType(GrantType.AUTHORIZATION_CODE);
    }
}
