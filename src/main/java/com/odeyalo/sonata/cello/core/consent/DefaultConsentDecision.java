package com.odeyalo.sonata.cello.core.consent;

/**
 * Default implementation of {@link ConsentDecision} that just holds {@link com.odeyalo.sonata.cello.core.consent.ConsentDecision.Decision}
 * @param decision - decision made by the resource owner
 */
public record DefaultConsentDecision(Decision decision) implements ConsentDecision {

    public static DefaultConsentDecision approved() {
        return new DefaultConsentDecision(Decision.APPROVED);
    }

    public static DefaultConsentDecision denied() {
        return new DefaultConsentDecision(Decision.DENIED);
    }

    public static DefaultConsentDecision from(boolean status) {
        return status ? approved() : denied();
    }
}
