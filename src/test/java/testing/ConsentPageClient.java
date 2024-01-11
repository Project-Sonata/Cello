package testing;

import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriBuilder;

/**
 * Used to send the requests to consent page endpoint(s)
 */
public final class ConsentPageClient {
    private final WebTestClient webTestClient;
    private final ConsentPageClientProps consentPageProps;

    public ConsentPageClient(WebTestClient webTestClient, ConsentPageClientProps consentPageClientProps) {
        this.webTestClient = webTestClient;
        this.consentPageProps = consentPageClientProps;
    }

    public WebTestClient.ResponseSpec getConsentPage() {
        String sessionId = consentPageProps.getAuthenticatedClientProps().getSessionId();

        WebTestClient.RequestHeadersSpec<?> requestBuilder = webTestClient.get()
                .uri(uriBuilder -> {
                    UriBuilder b = uriBuilder.path("/oauth2/consent");

                    if ( consentPageProps.getFlowId() != null ) {
                        b.queryParam("flow_id", consentPageProps.getFlowId()).build();
                    }

                    return b.build();
                });

        if ( sessionId != null ) {
            requestBuilder.cookie("SESSION", sessionId);
        }

        return requestBuilder.exchange();
    }

}
