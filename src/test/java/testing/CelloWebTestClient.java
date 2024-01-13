package testing;

import com.odeyalo.sonata.cello.core.DefaultOauth2ResponseTypes;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.odeyalo.sonata.cello.core.Oauth2RequestParameters.*;

/**
 * Web client to test endpoints for Cello project only.
 * It just delegates the calls to {@link WebTestClient} and wrap it.
 */
public final class CelloWebTestClient {
    private final WebTestClient webTestClient;

    public CelloWebTestClient(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    public ImplicitClient implicit() {
        return new ImplicitClient();
    }

    public ConsentPageClientProps.Builder consentPage() {
        return ConsentPageClientProps.builder(webTestClient);
    }

    public LoginClientProps.LoginClientPropsBuilder login() {
        return LoginClientProps.withWebTestClient(webTestClient);
    }

    @AllArgsConstructor
    @Value
    @Builder
    public static class AuthenticatedClientProps {
        String sessionId;

        public static AuthenticatedClientProps withSessionId(String sessionId) {
            return builder().sessionId(sessionId).build();
        }
    }

    /**
     * Client to send requests with implicit flow only
     */
    public class ImplicitClient {

        public WebTestClient.ResponseSpec sendRequest(ImplicitSpec requestBody) {
            return webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/oauth2/authorize")
                                    .queryParam(RESPONSE_TYPE, DefaultOauth2ResponseTypes.IMPLICIT.getName())
                                    .queryParam(CLIENT_ID, requestBody.getClientId())
                                    .queryParam(REDIRECT_URI, requestBody.getRedirectUri())
                                    .queryParam(SCOPE, requestBody.getScope())
                                    .queryParam(STATE, requestBody.getState())
                                    .build())
                    .exchange();

        }

        public WebTestClient.ResponseSpec sendUnknownAuthorizationRequest(ImplicitSpec requestBody) {
            return webTestClient.get()
                    .uri(builder ->
                            builder
                                    .path("/oauth2/authorize")
                                    .queryParam(CLIENT_ID, requestBody.getClientId())
                                    .queryParam(REDIRECT_URI, requestBody.getRedirectUri())
                                    .queryParam(SCOPE, requestBody.getScope())
                                    .queryParam(STATE, requestBody.getState())
                                    .build())
                    .exchange();

        }
    }

    public static class AuthenticatedClientBuilder<T> {
        private String sessionId;
        T parent;

        public AuthenticatedClientBuilder(T parent) {
            this.parent = parent;
        }

        public AuthenticatedClientBuilder<T> withSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public AuthenticatedClientProps build() {
            return AuthenticatedClientProps.withSessionId(sessionId);
        }

        public T and() {
            return parent;
        }
    }

    @Value
    @Builder
    public static class ImplicitSpec {
        @Nullable
        String clientId;
        @Nullable
        String redirectUri;
        @Nullable
        String scope;
        @Nullable
        String state;
    }
}
