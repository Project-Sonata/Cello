package com.odeyalo.sonata.cello.core.consent;

import org.jetbrains.annotations.NotNull;

/**
 * Represent always approved consent decision
 */
public class ApprovedConsentDecision implements ConsentDecision {

    @Override
    @NotNull
    public final Decision decision() {
        return Decision.APPROVED;
    }
}
