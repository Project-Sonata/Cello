package com.odeyalo.sonata.cello.core.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent the client credentials, such client id and client secret, as described in
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2.3">Client Authentication</a>
 *
 * Client is required for any client, when client secret can be nullable(public client is used)
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2ClientCredentials {
    @NotNull
    String clientId;
    @Nullable
    String clientSecret;

    public static Oauth2ClientCredentials withId(@NotNull String clientId) {
        return builder().clientId(clientId).build();
    }
}
