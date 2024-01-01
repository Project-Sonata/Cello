package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponseConverter;
import com.odeyalo.sonata.cello.core.consent.*;
import com.odeyalo.sonata.cello.core.responsetype.Oauth2ResponseTypeHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsentPageConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Oauth2ConsentApprovedHandler oauth2ConsentApprovedHandler(Oauth2ResponseTypeHandler oauth2ResponseTypeHandler,
                                                                     Oauth2AuthorizationResponseConverter oauth2AuthorizationResponseConverter) {
        return new ResponseTypeHandlerOauth2ConsentApprovedHandler(oauth2ResponseTypeHandler, oauth2AuthorizationResponseConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public ConsentDecisionResolver consentDecisionResolver() {
        return new FormDataConsentDecisionResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2ConsentDeniedHandler oauth2ConsentDeniedHandler() {
        return new DefaultOauth2ConsentDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public Oauth2ConsentSubmissionHandler oauth2ConsentSubmissionHandler(ConsentDecisionResolver consentDecisionResolver,
                                                                         Oauth2ConsentApprovedHandler approvedHandler,
                                                                         Oauth2ConsentDeniedHandler oauth2ConsentDeniedHandler) {
        return new DelegatingOauth2ConsentSubmissionHandler(consentDecisionResolver, approvedHandler, oauth2ConsentDeniedHandler);
    }
}
