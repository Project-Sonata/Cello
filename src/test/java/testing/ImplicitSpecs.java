package testing;

import testing.CelloWebTestClient.ImplicitSpec;

import java.util.UUID;

/**
 * Factory methods to create {@link ImplicitSpec}
 */
public final class ImplicitSpecs {

    private static final String EXISTING_CLIENT_ID = "123";
    private static final String ALLOWED_REDIRECT_URI = "http://localhost:4000";

    private static final String NOT_EXISTING_CLIENT_ID = "not_exist";
    private static final String UNALLOWED_REDIRECT_URI = "http://localhost:20120/not/allowed";

    public static ImplicitSpec valid() {
        return ImplicitSpec.builder()
                .clientId(EXISTING_CLIENT_ID)
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state("opaque")
                .build();
    }

    public static ImplicitSpec withoutClientId() {
        return ImplicitSpec.builder()
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }

    public static ImplicitSpec withInvalidClientId() {
        return ImplicitSpec.builder()
                .clientId(NOT_EXISTING_CLIENT_ID)
                .redirectUri(ALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }

    public static ImplicitSpec withUnallowedRedirectUri() {
        return ImplicitSpec.builder()
                .clientId(EXISTING_CLIENT_ID)
                .redirectUri(UNALLOWED_REDIRECT_URI)
                .scope("read, write")
                .state(UUID.randomUUID().toString())
                .build();
    }
}
