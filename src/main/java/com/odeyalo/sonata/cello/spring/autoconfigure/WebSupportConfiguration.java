package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.web.support.Jackson2ResponseWriter;
import com.odeyalo.sonata.cello.web.support.ResponseWriter;
import com.odeyalo.sonata.cello.web.support.AuthorizationRequestHandlerMethodArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;

@Configuration
public class WebSupportConfiguration {

    @Bean
    public ServerRequestCache serverRequestCache() {
        return new WebSessionServerRequestCache();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationRequestHandlerMethodArgumentResolver authorizationRequestHandlerMethodArgumentResolver(final Oauth2AuthorizationRequestRepository requestRepository) {
        return new AuthorizationRequestHandlerMethodArgumentResolver(requestRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseWriter responseWriter(@NotNull final ObjectMapper objectMapper) {
        return new Jackson2ResponseWriter(objectMapper);
    }
}
