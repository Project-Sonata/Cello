package testing.spring.configuration;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.RedirectUris;
import com.odeyalo.sonata.cello.core.client.*;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static testing.spring.configuration.RegisterOauth2Clients.*;

@Configuration
public class RegisteredClientRegistratorAutoConfiguration {

    @Bean
    public InMemoryOauth2RegisteredClientService inMemoryOauth2RegisteredClientService() {
        final Oauth2RegisteredClient client1 = Oauth2RegisteredClient.builder().clientType(ClientType.CONFIDENTIAL)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.of(DEFAULT_USERNAME, DEFAULT_PASSWORD))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .allowedRedirectUris(
                        RedirectUris.single(RedirectUri.create("http://localhost:4000"))
                ).build();

        final Oauth2RegisteredClient client2 = Oauth2RegisteredClient.builder().clientType(ClientType.CONFIDENTIAL)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.of(DEFAULT_CLIENT_2_USERNAME, DEFAULT_CLIENT_2_PASSWORD))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .allowedRedirectUris(
                        RedirectUris.single(RedirectUri.create("http://localhost:5000"))
                ).build();

        return new InMemoryOauth2RegisteredClientService(List.of(client1, client2));
    }

}
