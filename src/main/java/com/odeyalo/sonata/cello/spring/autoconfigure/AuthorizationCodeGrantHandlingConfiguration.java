package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.grant.AuthorizationCodeAccessTokenRequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationCodeGrantHandlingConfiguration {

    @Bean
    public AuthorizationCodeAccessTokenRequestConverter authorizationCodeAccessTokenRequestConverter() {
        return new AuthorizationCodeAccessTokenRequestConverter();
    }

}
