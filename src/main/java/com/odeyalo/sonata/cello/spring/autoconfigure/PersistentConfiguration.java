package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.WebSessionOauth2AuthorizationRequestRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Load beans to persist oauth2 requests
 */
@Configuration
public class PersistentConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Oauth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new WebSessionOauth2AuthorizationRequestRepository();
    }
}
