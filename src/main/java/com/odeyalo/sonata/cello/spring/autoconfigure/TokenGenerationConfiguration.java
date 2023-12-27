package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.token.access.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenGenerationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenStore accessTokenStore() {
        return new InMemoryAccessTokenStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpaqueOauth2AccessTokenValueGenerator opaqueOauth2AccessTokenValueGenerator() {
        return new UUIDOpaqueOauth2AccessTokenValueGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2AccessTokenGenerator oauth2AccessTokenGenerator(AccessTokenStore accessTokenStore,
                                                                 OpaqueOauth2AccessTokenValueGenerator tokenValueGenerator) {

        return new OpaquePersistentOauth2AccessTokenGenerator(accessTokenStore, tokenValueGenerator);
    }
}
