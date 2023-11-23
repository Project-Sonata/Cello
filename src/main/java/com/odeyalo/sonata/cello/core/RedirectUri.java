package com.odeyalo.sonata.cello.core;

import org.springframework.util.Assert;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Wrapper for Redirect uri
 * @param uri - uri to wrap, must be valid. Otherwise RedirectUri cannot be constructed and exception will be thrown
 */
public record RedirectUri(String uri) {

    public RedirectUri(String uri) {
        Assert.notNull(uri, "Uri must be not null!");
        Assert.state(isUriValid(uri), "Uri must be valid!");
        this.uri = uri;
    }

    public URI asUri() {
        return URI.create(uri);
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
