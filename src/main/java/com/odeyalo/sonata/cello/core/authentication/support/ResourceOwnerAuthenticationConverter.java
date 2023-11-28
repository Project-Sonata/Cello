package com.odeyalo.sonata.cello.core.authentication.support;

import com.odeyalo.sonata.cello.core.authentication.ResourceOwnerPreAuthentication;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import reactor.core.publisher.Mono;

/**
 * Convert the {@link ReactiveHttpRequest} to {@link ResourceOwnerPreAuthentication}
 */
public interface ResourceOwnerAuthenticationConverter {

    /**
     * Convert the current HttpRequest to ResourceOwnerPreAuthentication
     * @param httpRequest - current http request
     * @return - mono with ResourceOwnerAuthentication or empty Mono if not supported by implementation
     */
    Mono<ResourceOwnerPreAuthentication> convert(ReactiveHttpRequest httpRequest);

}
