package com.odeyalo.sonata.cello.core.responsetype.code;

import com.odeyalo.sonata.cello.core.RedirectUri;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import com.odeyalo.sonata.cello.core.responsetype.code.support.SupplierAuthorizationCodeGenerator;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationCodeResponseTypeHandlerTest {


    @Test
    void shouldGenerateAuthorizationCodeOnPermissionGranted() {
        final AuthorizationCodeResponseTypeHandler testable = new AuthorizationCodeResponseTypeHandler(
                new SupplierAuthorizationCodeGenerator(() -> "iLoveMiku")
        );

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