package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.InMemoryAuthorizationCodeRepository;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeAuthorizationRequestConverter;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeResponseConverter;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeResponseTypeHandler;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeService;
import com.odeyalo.sonata.cello.core.responsetype.code.support.DefaultAuthorizationCodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class AuthorizationCodeHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeAuthorizationRequestConverter authorizationCodeAuthorizationRequestConverter() {
        return new AuthorizationCodeAuthorizationRequestConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeResponseTypeHandler authorizationCodeResponseTypeHandler(@NotNull final AuthorizationCodeService codeGenerator,
                                                                                     @NotNull final Oauth2RegisteredClientService clientService) {
        return new AuthorizationCodeResponseTypeHandler(codeGenerator, clientService);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeResponseConverter authorizationCodeResponseConverter() {
        return new AuthorizationCodeResponseConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthorizationCodeService authorizationCodeGenerator(@Autowired(required = false) final Supplier<String> codeGenerator) {
        if ( codeGenerator == null ) {
            return new DefaultAuthorizationCodeService(new InMemoryAuthorizationCodeRepository());
        }
        return new DefaultAuthorizationCodeService(codeGenerator, new InMemoryAuthorizationCodeRepository());
    }

}
