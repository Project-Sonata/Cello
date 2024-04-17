package com.odeyalo.sonata.cello.spring.configuration.security;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequestRepository;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2AuthenticationMetadataRepository;
import com.odeyalo.sonata.cello.core.validation.Oauth2AuthorizationRequestValidator;
import com.odeyalo.sonata.cello.spring.configuration.security.customizer.CelloOauth2SecurityCustomizer;
import com.odeyalo.sonata.cello.web.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

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
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity,
                                                         ServerSecurityContextRepository securityContextRepository,
                                                         Oauth2AuthorizationRequestConverter oauth2AuthorizationRequestConverter,
                                                         Oauth2AuthorizationRequestValidator oauth2AuthorizationRequestValidator,
                                                         CelloOauth2ServerEndpointsSpec endpointsSpec,
                                                         Oauth2AuthorizationRequestRepository authorizationRequestRepository,
                                                         Oauth2AuthenticationMetadataRepository oauth2AuthenticationMetadataRepository) {

        ServerHttpSecurity serverHttpSecurity = httpSecurity
                .formLogin(formLoginSpecConfigurer)
                .csrf(csrfSpecConfigurer)
                .cors(corsSpecConfigurer)
                .addFilterBefore(new Oauth2ProviderAwareOauth2FlowAttributePopulatingFilter(endpointsSpec, oauth2AuthenticationMetadataRepository,
                         new PathPatternParserServerWebExchangeMatcher("/oauth2/login/{providerName}/callback")), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new AuthorizationRequestHandlerFilter(oauth2AuthorizationRequestConverter, oauth2AuthorizationRequestValidator, authorizationRequestRepository), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(new AuthenticationLoaderFilter(securityContextRepository), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpecConfigurer)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(authorizeExchangeSpecConfigurer)
                .securityMatcher(new CelloServerWebExchangeMatcher(endpointsSpec));

        oauth2SecurityCustomizer.customize(serverHttpSecurity);

        return serverHttpSecurity.build();

    }
}
