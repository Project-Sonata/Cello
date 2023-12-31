package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.DefaultAuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceOwnerAuthenticationConfiguration {
    private final Logger logger = LoggerFactory.getLogger("Cello-Resource-Owner-Configuration");


    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerService resourceOwnerService() {
        logger.debug("Using default InMemoryResourceOwnerService to access info about users");
        return new InMemoryResourceOwnerService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager(ResourceOwnerService resourceOwnerService) {
        logger.debug("Using username and password strategy to authenticate the resource owners");
        return new UsernamePasswordResourceOwnerAuthenticationManager(resourceOwnerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticator resourceOwnerAuthenticator(ResourceOwnerAuthenticationSuccessHandler successHandler,
                                                                 ResourceOwnerAuthenticationFailureHandler failureHandler,
                                                                 ResourceOwnerAuthenticationManager authenticationManager) {
        return new HandlerResourceOwnerAuthenticator(authenticationManager, successHandler, failureHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationFailureHandler resourceOwnerAuthenticationFailureHandler() {
        return new BadRequestStatusResourceOwnerAuthenticationFailureHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationSuccessHandler resourceOwnerAuthenticationSuccessHandler() {
        return new ConsentPageRedirectingResourceOwnerAuthenticationSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationPageProvider authenticationPageProvider() {
        return new DefaultAuthenticationPageProvider();
    }
}
