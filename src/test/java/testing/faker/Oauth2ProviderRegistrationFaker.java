package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.oauth2.Oauth2ProviderRegistration;
import org.apache.commons.lang3.RandomStringUtils;

public final class Oauth2ProviderRegistrationFaker {
    private final Oauth2ProviderRegistration.Oauth2ProviderRegistrationBuilder builder = Oauth2ProviderRegistration.builder();
    private final Faker faker = Faker.instance();

    public Oauth2ProviderRegistrationFaker() {
        builder.providerUri(faker.internet().url())
                .tokenEndpoint(faker.internet().url())
                .redirectUri(faker.internet().url())
                .clientSecret(RandomStringUtils.randomAlphanumeric(22))
                .clientId(RandomStringUtils.randomAlphanumeric(33))
                .name(faker.options().option("google", "github", "facebook", "spotify", "VK ID"))
                .scopes(ScopeContainer.fromArray(SimpleScope.withName("read"), SimpleScope.withName("write")));
    }

    public static Oauth2ProviderRegistrationFaker create() {
        return new Oauth2ProviderRegistrationFaker();
    }


    public Oauth2ProviderRegistrationFaker withTokenEndpoint(final String uri) {
        builder.tokenEndpoint(uri);
        return this;
    }

    public Oauth2ProviderRegistrationFaker withClientId(final String clientId) {
        builder.clientId(clientId);
        return this;
    }

    public Oauth2ProviderRegistrationFaker withClientSecret(final String clientSecret) {
        builder.clientSecret(clientSecret);
        return this;
    }

    public Oauth2ProviderRegistrationFaker withRedirectUri(final String redirectUri) {
        builder.redirectUri(redirectUri);
        return this;
    }

    public Oauth2ProviderRegistration get() {
        return builder.build();
    }
}
