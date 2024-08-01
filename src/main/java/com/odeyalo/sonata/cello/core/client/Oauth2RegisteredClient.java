package com.odeyalo.sonata.cello.core.client;

import com.odeyalo.sonata.cello.core.RedirectUris;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * POJO about client that has been registered in Cello.
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2RegisteredClient {
    @NotNull
    Oauth2ClientCredentials credentials;
    @NotNull
    @Builder.Default
    Oauth2ClientInfo oauth2ClientInfo = EmptyOauth2ClientInfo.create();
    @NotNull
    ClientProfile clientProfile;
    @NotNull
    ClientType clientType;
    @NotNull
    @Builder.Default
    RedirectUris allowedRedirectUris = RedirectUris.empty();

    public boolean isSame(@NotNull final Oauth2RegisteredClient other) {
        return Objects.equals(other.getCredentials().getClientId(), credentials.getClientId());

    }

    public boolean checkCredentials(@NotNull final Oauth2ClientCredentials otherCredentials) {
        return credentials.equals(otherCredentials);
    }
}
