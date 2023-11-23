package testing.faker;

import com.github.javafaker.Faker;
import com.odeyalo.sonata.cello.core.client.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Oauth2RegisteredClientFaker {
    Oauth2RegisteredClient.Oauth2RegisteredClientBuilder builder = Oauth2RegisteredClient.builder();
    Faker faker = Faker.instance();

    public Oauth2RegisteredClientFaker() {
        builder
                .clientProfile(faker.options().option(ClientProfile.class))
                .clientType(faker.options().option(ClientType.class))
                .oauth2ClientInfo(EmptyOauth2ClientInfo.create())
                .credentials(Oauth2ClientCredentialsFaker.create().get());
    }

    public static Oauth2RegisteredClientFaker create() {
        return new Oauth2RegisteredClientFaker();
    }

    public Oauth2RegisteredClient get() {
        return builder.build();
    }

    // Wrapper methods

    public Oauth2RegisteredClientFaker clientType(ClientType clientType) {
        builder.clientType(clientType);
        return this;
    }

    public Oauth2RegisteredClientFaker clientProfile(ClientProfile clientProfile) {
        builder.clientProfile(clientProfile);
        return this;
    }

    public Oauth2RegisteredClientFaker oauth2ClientInfo(Oauth2ClientInfo oauth2ClientInfo) {
        builder.oauth2ClientInfo(oauth2ClientInfo);
        return this;
    }

    public Oauth2RegisteredClientFaker credentials(Oauth2ClientCredentials credentials) {
        builder.credentials(credentials);
        return this;
    }
}
