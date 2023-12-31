package com.odeyalo.sonata.cello.core.consent;

import org.jetbrains.annotations.NotNull;

/**
 * Represent always denied consent by the resource owner
 */
public class DeniedConsentDecision implements ConsentDecision {

    @Override
    @NotNull
    public final Decision decision() {
        return Decision.DENIED;
    }
}
