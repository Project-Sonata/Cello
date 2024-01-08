package com.odeyalo.sonata.cello.spring.configuration;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.spring.configuration.security.customizer.CelloOauth2SecurityCustomizer;
import com.odeyalo.sonata.cello.web.AuthenticationLoaderFilter;
import com.odeyalo.sonata.cello.web.AuthorizationRequestHandlerFilter;
import com.odeyalo.sonata.cello.web.Oauth2FlowAttributePopulatingFilter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfiguration {
    @Autowired
    Customizer<ServerHttpSecurity.CsrfSpec> csrfSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.FormLoginSpec> formLoginSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.CorsSpec> corsSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.ExceptionHandlingSpec> exceptionHandlingSpecConfigurer;
    @Autowired
    Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> authorizeExchangeSpecConfigurer;
    @Autowired
    CelloOauth2SecurityCustomizer oauth2SecurityCustomizer;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         ServerSecurityContextRepository securityContextRepository,
                                                         Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter,
                                                         Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator,
                                                         Oauth2AuthorizationRequestRepository authorizationRequestRepository) {

        ServerHttpSecurity serverHttpSecurity = httpSecurity
                .formLogin(formLoginSpecConfigurer)
                .csrf(csrfSpecConfigurer)
                .cors(corsSpecConfigurer)
                .addFilterBefore(new Oauth2FlowAttributePopulatingFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new AuthorizationRequestHandlerFilter(oauth2AuthorizationRequestConverter, oauth2AuthorizationRequestValidator, authorizationRequestRepository), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(new AuthenticationLoaderFilter(securityContextRepository), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpecConfigurer)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(authorizeExchangeSpecConfigurer);

        oauth2SecurityCustomizer.customize(serverHttpSecurity);

        return serverHttpSecurity.build();

    }
}
