package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.validation.ImplicitOauth2AuthorizationRequestValidationProvider;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidationProvider;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.core.validation.ProviderOauth2AuthorizationRequestValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RequestValidationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator(List<Oauth2AuthorizationRequestValidationProvider> providers) {
        return new ProviderOauth2AuthorizationRequestValidator(providers);
    }

    @Bean
    @ConditionalOnMissingBean
    public ImplicitOauth2AuthorizationRequestValidationProvider implicitValidationProvider(Oauth2RegisteredClientService oauth2RegisteredClientService) {
        return new ImplicitOauth2AuthorizationRequestValidationProvider(oauth2RegisteredClientService);
    }

}
