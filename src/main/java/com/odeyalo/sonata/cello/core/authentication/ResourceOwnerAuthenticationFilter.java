package com.odeyalo.sonata.cello.core.authentication;

import com.odeyalo.sonata.cello.core.authentication.support.FormDataResourceOwnerAuthenticationConverter;
import com.odeyalo.sonata.cello.core.authentication.support.ResourceOwnerAuthenticationConverter;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
public class ResourceOwnerAuthenticationFilter {


    private final ResourceOwnerAuthenticationConverter converter;
    private final ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager;
    private final ResourceOwnerAuthenticationSuccessHandler successHandler;

    public ResourceOwnerAuthenticationFilter(ResourceOwnerAuthenticationConverter converter,
                                             ResourceOwnerAuthenticationManager resourceOwnerAuthenticationManager,
                                             ResourceOwnerAuthenticationSuccessHandler successHandler) {
        this.converter = converter;

        this.resourceOwnerAuthenticationManager = resourceOwnerAuthenticationManager;
        this.successHandler = successHandler;
    }

    public Mono<Void> doFilter(ReactiveHttpRequest request, ReactiveHttpResponse response) {

        return converter.convert(request)
                .flatMap(preAuthentication -> resourceOwnerAuthenticationManager.attemptAuthentication(preAuthentication)
                        .flatMap(resourceOwner -> onSuccess(request, response, resourceOwner))
                        .then()
                );
    }

    private Mono<Void> onSuccess(ReactiveHttpRequest request, ReactiveHttpResponse response, AuthenticatedResourceOwner owner) {
        return successHandler.onSuccess(request, response, owner);
    }
}
