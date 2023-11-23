package testing.faker;

import com.odeyalo.sonata.cello.core.client.Oauth2ClientCredentials;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Oauth2ClientCredentialsFaker {
    Oauth2ClientCredentials.Oauth2ClientCredentialsBuilder builder = Oauth2ClientCredentials.builder();

    public Oauth2ClientCredentialsFaker() {
        builder
                .clientId(RandomStringUtils.randomAlphanumeric(22))
                .clientSecret(RandomStringUtils.randomAlphanumeric(22));
    }

    public static Oauth2ClientCredentialsFaker create() {
        return new Oauth2ClientCredentialsFaker();
    }

    public Oauth2ClientCredentials get() {
        return builder.build();
    }

    // Wrapper methods

    public Oauth2ClientCredentialsFaker clientId(@NotNull String clientId) {
        builder.clientId(clientId);
        return this;
    }

    public Oauth2ClientCredentialsFaker clientSecret(@Nullable String clientSecret) {
        builder.clientSecret(clientSecret);
        return this;
    }
}
