package testing;

import com.odeyalo.sonata.cello.core.consent.ConsentDecision;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriBuilder;

/**
 * Used to send the requests to consent page endpoint(s)
 */
public final class ConsentPageClient {
    private final WebTestClient webTestClient;
    private final ConsentPageClientProps consentPageProps;

    private static final String SUBMIT_CONSENT_PAGE_URI = "/oauth2/consent";
    private static final String FLOW_ID_PARAM_NAME = "flow_id";
    private static final String CONSENT_DECISION_FORM_DATA_KEY = "action";
    private static final String APPROVED_CONSENT_VALUE = "approved";
    private static final String DENIED_CONSENT_VALUE = "denied";
    private static final String GET_CONSENT_PAGE_URI = "/oauth2/consent";
    private static final String SESSION_COOKIE_NAME = "SESSION";


    public ConsentPageClient(WebTestClient webTestClient, ConsentPageClientProps consentPageClientProps) {
        this.webTestClient = webTestClient;
        this.consentPageProps = consentPageClientProps;
    }

    public WebTestClient.ResponseSpec getConsentPage() {
        String sessionId = consentPageProps.getAuthenticatedClientProps().getSessionId();

        WebTestClient.RequestHeadersSpec<?> requestBuilder = webTestClient.get()
                .uri(uriBuilder -> {
                    UriBuilder b = uriBuilder.path(GET_CONSENT_PAGE_URI);

                    if ( consentPageProps.getFlowId() != null ) {
                        b.queryParam(FLOW_ID_PARAM_NAME, consentPageProps.getFlowId());
                    }

                    return b.build();
                });

        if ( sessionId != null ) {
            requestBuilder.cookie(SESSION_COOKIE_NAME, sessionId);
        }

        return requestBuilder.exchange();
    }

    public WebTestClient.ResponseSpec submitConsentDecision(ConsentDecision consentDecision) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        String decision = consentDecision.decision() == ConsentDecision.Decision.APPROVED
                ? APPROVED_CONSENT_VALUE
                : DENIED_CONSENT_VALUE;


        formData.add(CONSENT_DECISION_FORM_DATA_KEY, decision);

        if (consentPageProps.getFlowId() != null) {
            formData.add(FLOW_ID_PARAM_NAME, consentPageProps.getFlowId());
        }

        WebTestClient.RequestHeadersSpec<?> requestBuilder = webTestClient.post()
                .uri(SUBMIT_CONSENT_PAGE_URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData));

        String sessionId = consentPageProps.getAuthenticatedClientProps().getSessionId();

        if ( sessionId != null ) {
            requestBuilder.cookie(SESSION_COOKIE_NAME, sessionId);
        }

        return requestBuilder.exchange();
    }
}
