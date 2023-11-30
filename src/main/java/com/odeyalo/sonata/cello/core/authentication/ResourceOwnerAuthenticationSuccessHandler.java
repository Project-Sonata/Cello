package com.odeyalo.sonata.cello.core.authentication;

import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpResponse;
import reactor.core.publisher.Mono;

public interface ResourceOwnerAuthenticationSuccessHandler {

    Mono<Void> onSuccess(ReactiveHttpRequest request, ReactiveHttpResponse response, AuthenticatedResourceOwner resourceOwner);

}
