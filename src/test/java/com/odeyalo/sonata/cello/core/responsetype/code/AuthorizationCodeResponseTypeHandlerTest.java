package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.InMemoryAuthorizationCodeRepository;
import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import com.odeyalo.sonata.cello.core.client.registration.InMemoryOauth2RegisteredClientService;
import com.odeyalo.sonata.cello.core.responsetype.code.support.DefaultAuthorizationCodeService;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import testing.faker.Oauth2RegisteredClientFaker;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationCodeResponseTypeHandlerTest {

    @Test
    void shouldGenerateAuthorizationCodeOnPermissionGranted() {
        final AuthorizationCodeResponseTypeHandler testable = new AuthorizationCodeResponseTypeHandler(
                new DefaultAuthorizationCodeService(() -> "iLoveMiku", new InMemoryAuthorizationCodeRepository()),
                new InMemoryOauth2RegisteredClientService(Oauth2RegisteredClientFaker.create()
                        .credentials(Oauth2ClientCredentials.withId("123")).get()));

        final AuthorizationCodeRequest authorizationCodeRequest = AuthorizationCodeRequest.builder()
                .clientId("123")
                .redirectUri(RedirectUri.create("http://localhost:3000/callback"))
                .build();

        testable.permissionGranted(authorizationCodeRequest, ResourceOwner.withPrincipalOnly("odeyalo"))
                .cast(AuthorizationCodeResponse.class)
                .as(StepVerifier::create)
                .assertNext(response -> assertThat(response.getAuthorizationCode()).isEqualTo("iLoveMiku"))
                .verifyComplete();
    }
}