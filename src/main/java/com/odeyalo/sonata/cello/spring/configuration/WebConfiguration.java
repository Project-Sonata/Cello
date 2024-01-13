package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.web.CelloOauth2ServerEndpointsSpec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Contain beans for web
 */
@Configuration
public class WebConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CelloOauth2ServerEndpointsSpec celloOauth2ServerEndpointsSpec() {
        return CelloOauth2ServerEndpointsSpec.defaultSpec();
    }
}
