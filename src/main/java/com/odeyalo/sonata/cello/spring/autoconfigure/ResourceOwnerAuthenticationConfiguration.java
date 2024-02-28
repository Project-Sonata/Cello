package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.authentication.AuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.DefaultAuthenticationPageProvider;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.*;
import com.odeyalo.sonata.cello.web.CelloOauth2ServerEndpointsSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

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
    public AuthenticationCredentialsConverter authenticationCredentialsConverter() {
        return new FormDataUsernamePasswordAuthenticationCredentialsConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationProvider resourceOwnerAuthenticationProvider(ResourceOwnerService resourceOwnerService) {
        return new UsernamePasswordResourceOwnerAuthenticationProvider(resourceOwnerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager(AuthenticationCredentialsConverter converter,
                                                                                 ResourceOwnerAuthenticationProvider provider) {
        logger.debug("Using {} to handle the authentication", provider);
        return new ProviderDelegateResourceOwnerAuthenticationManager(converter, provider);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticator resourceOwnerAuthenticator(ResourceOwnerAuthenticationSuccessHandler successHandler,
                                                                 ResourceOwnerAuthenticationFailureHandler failureHandler,
                                                                 ResourceOwnerAuthenticationManager authenticationManager,
                                                                 ServerSecurityContextRepository resourceOwnerServerSecurityContextRepository) {
        return new HandlerResourceOwnerAuthenticator(authenticationManager, successHandler, failureHandler, resourceOwnerServerSecurityContextRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ServerSecurityContextRepository resourceOwnerServerSecurityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationFailureHandler resourceOwnerAuthenticationFailureHandler() {
        return new BadRequestStatusResourceOwnerAuthenticationFailureHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceOwnerAuthenticationSuccessHandler resourceOwnerAuthenticationSuccessHandler(CelloOauth2ServerEndpointsSpec endpointsSpec) {
        return new ConsentPageRedirectingResourceOwnerAuthenticationSuccessHandler(endpointsSpec);
    }
  
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationPageProvider authenticationPageProvider(CelloOauth2ServerEndpointsSpec endpointsSpec) {
        return new DefaultAuthenticationPageProvider(endpointsSpec);
    }
}
