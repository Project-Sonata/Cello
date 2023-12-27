package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.responsetype.implicit.ImplicitOauth2ResponseTypeHandler;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImplicitHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ImplicitOauth2AuthorizationRequestConverter implicitOauth2AuthorizationRequestConverter() {
        return new ImplicitOauth2AuthorizationRequestConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImplicitOauth2AuthorizationResponseConverter implicitOauth2AuthorizationResponseConverter() {
        return new ImplicitOauth2AuthorizationResponseConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImplicitOauth2ResponseTypeHandler implicitOauth2ResponseTypeHandler(Oauth2AccessTokenGenerator accessTokenGenerator,
                                                                               Oauth2RegisteredClientService oauth2RegisteredClientService) {
        return new ImplicitOauth2ResponseTypeHandler(accessTokenGenerator, oauth2RegisteredClientService);
    }
}
