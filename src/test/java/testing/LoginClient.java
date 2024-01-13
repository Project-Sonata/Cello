package testing;

import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * Used to send requests to /login endpoint only
 */
public final class LoginClient {
    private final WebTestClient webTestClient;
    private final LoginClientProps props;

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String LOGIN_ENDPOINT_URI = "/oauth2/login";
    private static final String FLOW_ID_PARAM = "flow_id";
    private static final String SESSION_COOKIE_NAME = "SESSION";

    public LoginClient(WebTestClient webTestClient, LoginClientProps props) {
        this.webTestClient = webTestClient;
        this.props = props;
    }


    @NotNull
    public WebTestClient.ResponseSpec usernamePasswordLogin(@NotNull String username, @NotNull String password) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_KEY, username);
        formData.add(PASSWORD_KEY, password);

        if ( props.getFlowId() != null ) {
            formData.add(FLOW_ID_PARAM, props.getFlowId());
        }

        WebTestClient.RequestHeadersSpec<?> requestBuilder = webTestClient.post()
                .uri(LOGIN_ENDPOINT_URI)
                .body(BodyInserters.fromFormData(formData));

        if ( props.getSessionId() != null ) {
            requestBuilder.cookie(SESSION_COOKIE_NAME, props.getSessionId());
        }

        return requestBuilder.exchange();
    }
}
