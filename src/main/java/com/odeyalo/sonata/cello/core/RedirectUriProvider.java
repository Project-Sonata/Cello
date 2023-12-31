package com.odeyalo.sonata.cello.core;

import org.jetbrains.annotations.NotNull;

/**
 * Represent that authorization grant(response type) contain the redirect uri that can be used
 */
public interface RedirectUriProvider {

    @NotNull
    RedirectUri getRedirectUri();

}
