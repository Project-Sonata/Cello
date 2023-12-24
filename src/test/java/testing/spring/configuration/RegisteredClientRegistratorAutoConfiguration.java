package testing.spring.configuration;

import com.odeyalo.sonata.cello.core.client.*;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisteredClientRegistratorAutoConfiguration {

    @Bean
    public InMemoryOauth2RegisteredClientService inMemoryOauth2RegisteredClientService() {
        Oauth2RegisteredClient client = Oauth2RegisteredClient.builder().clientType(ClientType.PUBLIC)
                .clientProfile(ClientProfile.WEB_APPLICATION)
                .credentials(Oauth2ClientCredentials.withId("123"))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .build();

        return new InMemoryOauth2RegisteredClientService(
            client
        );
    }

}
