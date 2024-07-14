package testing;

import java.util.UUID;

public final class AuthorizationCodeSpecs {

    public static final String EXISTING_CLIENT_ID = "123";
    public static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    public static final String NOT_EXISTING_CLIENT_ID = "not_exist";
    public static final String UNALLOWED_REDIRECT_URI = "http://localhost:20120/not/allowed";

    public static CelloWebTestClient.AuthorizationCodeSpec valid() {
        return CelloWebTestClient.AuthorizationCodeSpec.builder()
                .clientId(EXISTING_CLIENT_ID)
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state("opaque")
                .build();
    }

    public static CelloWebTestClient.AuthorizationCodeSpec withoutClientId() {
        return CelloWebTestClient.AuthorizationCodeSpec.builder()
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }

    public static CelloWebTestClient.AuthorizationCodeSpec withInvalidClientId() {
        return CelloWebTestClient.AuthorizationCodeSpec.builder()
                .clientId(NOT_EXISTING_CLIENT_ID)
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }

    public static CelloWebTestClient.AuthorizationCodeSpec withUnallowedRedirectUri() {
        return CelloWebTestClient.AuthorizationCodeSpec.builder()
                .clientId(EXISTING_CLIENT_ID)
                .redirectUri(UNALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }
}
