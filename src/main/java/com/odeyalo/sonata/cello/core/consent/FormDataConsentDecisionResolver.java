package com.odeyalo.sonata.cello.core.consent;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolve the {@link ConsentDecision} from the form data of {@link ServerWebExchange}.
 * The default behavior is:
 * <ol>
 *   <li>Get the consent decision by key 'action' from the form data of {@link ServerWebExchange}.</li>
 *   <li>If the obtained value is null, return an empty {@link Mono}.</li>
 *   <li>If the obtained value is not null, check if the value is equal to 'approved'.</li>
 *   <li>If the value is equal to 'approved', then the consent is considered approved.</li>
 *   <li>Otherwise, the consent is considered denied.</li>
 * </ol>
 */
public class FormDataConsentDecisionResolver implements ConsentDecisionResolver {
    public static final String DEFAULT_APPROVED_VALUE = "approved";
    public static final String DEFAULT_CONSENT_DECISION_FORM_DATA_KEY = "action";

    private String consentDecisionFormDataKey = DEFAULT_CONSENT_DECISION_FORM_DATA_KEY;
    private String approvedValue = DEFAULT_APPROVED_VALUE;

    @Override
    @NotNull
    public Mono<ConsentDecision> resolveConsentDecision(@NotNull ServerWebExchange httpExchange) {
        return httpExchange.getFormData()
                .mapNotNull(this::resolveDecision);
    }

    @Nullable
    private DefaultConsentDecision resolveDecision(MultiValueMap<String, String> formData) {
        String isApprovedFormValue = formData.getFirst(consentDecisionFormDataKey);

        if ( isApprovedFormValue == null ) {
            return null;
        }

        boolean isApproved = StringUtils.equalsIgnoreCase(isApprovedFormValue, approvedValue);

        return DefaultConsentDecision.from(isApproved);
    }

    public void setConsentDecisionFormDataKey(@NotNull String consentDecisionFormDataKey) {
        this.consentDecisionFormDataKey = consentDecisionFormDataKey;
    }

    public void setApprovedValue(@NotNull String approvedValue) {
        this.approvedValue = approvedValue;
    }
}
