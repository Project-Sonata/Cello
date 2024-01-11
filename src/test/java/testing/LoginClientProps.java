package testing;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Properties for {@link LoginClient}
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginClientProps {
    String flowId;
    String sessionId;
    WebTestClient webTestClient;

    private static LoginClientPropsBuilder builder() {
        return new LoginClientPropsBuilder();
    }

    public static LoginClientPropsBuilder withWebTestClient(WebTestClient client) {
        return builder().webTestClient(client);
    }

    public static class LoginClientPropsBuilder {

        public LoginClient ready() {
            return new LoginClient(webTestClient, this.build());
        }
    }
}
