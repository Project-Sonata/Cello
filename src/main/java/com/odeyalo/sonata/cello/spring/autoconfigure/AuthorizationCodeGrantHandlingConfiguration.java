package com.odeyalo.sonata.cello.spring.autoconfigure;

import com.odeyalo.sonata.cello.core.grant.AuthorizationCodeAccessTokenRequestConverter;
import com.odeyalo.sonata.cello.core.grant.AuthorizationCodeGrantHandler;
import com.odeyalo.sonata.cello.core.responsetype.code.support.AuthorizationCodeService;
import com.odeyalo.sonata.cello.core.token.access.Oauth2AccessTokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationCodeGrantHandlingConfiguration {

    @Bean
    public AuthorizationCodeAccessTokenRequestConverter authorizationCodeAccessTokenRequestConverter() {
        return new AuthorizationCodeAccessTokenRequestConverter();
    }

    @Bean
    public AuthorizationCodeGrantHandler authorizationCodeGrantHandler(final AuthorizationCodeService authorizationCodeService,
                                                                       final Oauth2AccessTokenGenerator accessTokenGenerator) {
        return new AuthorizationCodeGrantHandler(authorizationCodeService, accessTokenGenerator);
    }

}
