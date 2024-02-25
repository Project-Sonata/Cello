package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.authentication.oauth2.InMemoryOauth2ProviderRegistrationRepository;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration to configure the beans for third-party Oauth2 providers
 */
@Configuration
public class ThirdPartyOauth2ProvidersConfiguration {
    private final Logger logger = LoggerFactory.getLogger(ThirdPartyOauth2ProvidersConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public Oauth2ProviderRegistrationRepository oauth2ProviderRegistrationRepository() {
        logger.warn("Using empty [Oauth2ProviderRegistrationRepository], to override the default bean");
        logger.warn("Create a bean of Oauth2ProviderRegistrationRepository");
        return new InMemoryOauth2ProviderRegistrationRepository();
    }
}
