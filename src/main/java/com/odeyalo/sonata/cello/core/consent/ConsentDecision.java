package com.odeyalo.sonata.cello.core.consent;

import org.jetbrains.annotations.NotNull;

/**
 * Represent the decision made by the resource owner about granting the authorization to the client
 */
public interface ConsentDecision {
    /**
     * @return - decision that was made by the resource owner. Not null
     */
    @NotNull
    Decision decision();

    enum Decision {
        APPROVED,
        DENIED
    }
}
