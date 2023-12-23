package com.odeyalo.sonata.cello.core;

import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wrapper for Redirect uri
 *
 * @param uriString - uri as string to wrap, must be valid. Otherwise, RedirectUri cannot be constructed and exception will be thrown
 */
public record RedirectUri(String uriString) {

    public RedirectUri {
        Assert.notNull(uriString, "Uri must be not null!");
        Assert.state(isUriValid(uriString), "Uri must be valid!");
    }

    public static RedirectUri create(String uri) {
        return new RedirectUri(uri);
    }

    private boolean isUriValid(String uri) {
        try {
            new URI(uri);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
}
