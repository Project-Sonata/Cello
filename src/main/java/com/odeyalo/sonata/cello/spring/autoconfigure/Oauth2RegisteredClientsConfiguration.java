package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.RedirectUris;
import com.odeyalo.sonata.cello.core.client.*;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Oauth2RegisteredClientsConfiguration {
    private static final String POSTMAN_CLIENT_SECRET = "postman";
    private static final String POSTMAN_CLIENT_ID = "postman";
    private static final List<String> POSTMAN_ALLOWED_REDIRECT_URIS = List.of("https://oauth.pstmn.io/v1/callback");

    private final Logger logger = LoggerFactory.getLogger("Cello-Oauth2-Clients-Configuration");

    @Bean
    @ConditionalOnMissingBean
    public Oauth2RegisteredClientService oauth2RegisteredClientService(Oauth2RegisteredClient oauth2RegisteredClient) {
        return new InMemoryOauth2RegisteredClientService(oauth2RegisteredClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2RegisteredClient postmanOauth2Client() {
        logger.warn("============================");
        logger.warn("Using the postman oauth2 client as default");
        logger.warn("Client id: {}, client secret: {}, allowed redirect uris: {}", POSTMAN_CLIENT_ID, POSTMAN_CLIENT_SECRET, POSTMAN_ALLOWED_REDIRECT_URIS);
        logger.warn("============================");

        List<RedirectUri> allowedRedirectUris = POSTMAN_ALLOWED_REDIRECT_URIS.stream()
                .map(RedirectUri::create)
                .toList();

        return Oauth2RegisteredClient.builder()
                .credentials(Oauth2ClientCredentials.of(POSTMAN_CLIENT_ID, POSTMAN_CLIENT_SECRET))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .clientType(ClientType.CONFIDENTIAL)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .allowedRedirectUris(RedirectUris.fromCollection(allowedRedirectUris))
                .build();
    }
}
