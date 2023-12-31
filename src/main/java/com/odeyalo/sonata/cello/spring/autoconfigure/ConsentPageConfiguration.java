package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.consent.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;

@Configuration
public class ConsentPageConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Oauth2ConsentApprovedHandler oauth2ConsentApprovedHandler(ServerRequestCache serverRequestCache) {
        return new RequestCacheRedirectingOauth2ConsentApprovedHandler(serverRequestCache);
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
