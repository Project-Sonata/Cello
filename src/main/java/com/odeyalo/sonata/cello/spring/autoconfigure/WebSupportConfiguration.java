package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.web.support.AuthorizationRequestHandlerMethodArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSupportConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationRequestHandlerMethodArgumentResolver authorizationRequestHandlerMethodArgumentResolver(Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter,
                                                                                                               Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator) {
        return new AuthorizationRequestHandlerMethodArgumentResolver(oauth2AuthorizationRequestConverter, oauth2AuthorizationRequestValidator);
    }
}
