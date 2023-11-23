package com.odeyalo.sonata.cello.core.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * POJO about client that has been registered in Cello.
 */
@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class Oauth2RegisteredClient {
    Oauth2ClientCredentials credentials;
    Oauth2ClientInfo oauth2ClientInfo;
    ClientProfile clientProfile;
    ClientType clientType;
}
