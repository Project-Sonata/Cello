package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.client.authentication.FormDataOauth2ClientResolverStrategy;
import com.odeyalo.sonata.cello.core.client.authentication.Oauth2ClientResolverStrategy;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientAuthenticationStrategiesConfiguration {

    @Bean
    public Oauth2ClientResolverStrategy oauth2ClientResolverStrategy(final Oauth2RegisteredClientService clientService) {
        return new FormDataOauth2ClientResolverStrategy(clientService);
    }

}
