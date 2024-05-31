package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistrationRepository;
import com.odeyalo.sonata.cello.core.authentication.oauth2.dto.DefaultOauth2AccessTokenResponse;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.AuthorizationCodeExchange;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.DefaultAuthorizationCodeExchange;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.DefaultOauth2AccessTokenResponseHandler;
import com.odeyalo.sonata.cello.core.authentication.oauth2.exchange.Oauth2AccessTokenResponseHandler;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.StaticCallbackOauth2ProviderNameResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class Oauth2ProvidersConfiguration {

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public StaticCallbackOauth2ProviderNameResolver staticCallbackOauth2ProviderNameResolver() {
        return StaticCallbackOauth2ProviderNameResolver.alwaysReturn("google");
    }

    @Bean
    public DefaultOauth2AccessTokenResponseHandler responseHandler() {
        return new DefaultOauth2AccessTokenResponseHandler(new DefaultOauth2AccessTokenResponse.Factory());
    }

    /**
     * Used to auto-register {@link AuthorizationCodeExchange} dynamically based on {@link Oauth2ProviderRegistrationRepository#findAll()}
     */
    @Bean
    public List<AuthorizationCodeExchange> defaultAuthorizationCodeExchange(
            final Oauth2ProviderRegistrationRepository providerRegistrationRepository,
            final WebClient webClient,
            final DefaultOauth2AccessTokenResponseHandler handler) {


        return providerRegistrationRepository.findAll()
                .map(it -> {
                    final AuthorizationCodeExchange bean = authorizationCodeExchange(it, webClient, handler);
                    final String beanName = it.getName() + StringUtils.capitalize(bean.getClass().getSimpleName());
                    this.beanFactory.initializeBean(bean, beanName);
                    this.beanFactory.autowireBean(bean);
                    this.beanFactory.registerSingleton(beanName, bean);
                    return bean;
                })
                .log()
                .collectList()
                .block();
    }

    public AuthorizationCodeExchange authorizationCodeExchange(final Oauth2ProviderRegistration providerRegistration,
                                                               final WebClient webClient,
                                                               final Oauth2AccessTokenResponseHandler responseHandler) {
        return new DefaultAuthorizationCodeExchange(providerRegistration, webClient, responseHandler);
    }
}
