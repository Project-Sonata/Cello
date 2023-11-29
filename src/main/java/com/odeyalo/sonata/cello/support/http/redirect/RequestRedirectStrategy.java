package com.odeyalo.sonata.cello.support.http.redirect;

import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Interface used as strategy to redirect the current request to another URI
 */
public interface RequestRedirectStrategy {
    /**
     * redirect the current request to provided location
     * @param request - current request
     * @param response - response associated with current request
     * @param location - location to redirect to
     * @return - completed or failed Mono
     */
    Mono<Void> sendRedirect(ReactiveHttpRequest request, ReactiveHttpResponse response, URI location);

}
