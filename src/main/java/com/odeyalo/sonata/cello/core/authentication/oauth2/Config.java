package com.odeyalo.sonata.cello.core.authentication.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
public class Config {

    @Bean
    public Map<String, Oauth2ProviderRegistration> oauth2ProviderRegistrationMap() {
        return Map.of("google", Oauth2ProviderRegistration.builder()
                .providerUri("https://accounts.google.com/o/oauth2/v2/auth")
                .clientId("odeyalooo")
                .clientSecret("secret")
                .redirectUri("http://localhost:3000")
                .scopes(Set.of("read", "write"))
                .build()
        );
    }
}
