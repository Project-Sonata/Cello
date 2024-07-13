package com.odeyalo.sonata.cello.core.responsetype;

import com.odeyalo.sonata.cello.core.Oauth2AuthorizationRequest;
import com.odeyalo.sonata.cello.core.Oauth2AuthorizationResponse;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.ResourceOwner;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Primary
public final class CompositeOauth2ResponseTypeHandler implements Oauth2ResponseTypeHandler {
    private final List<Oauth2ResponseTypeHandler> handlers;

    public CompositeOauth2ResponseTypeHandler(final List<Oauth2ResponseTypeHandler> handlers) {
        this.handlers = handlers;
        System.out.println(handlers);
    }

    @Override
    public @NotNull Mono<Boolean> supports(@NotNull final Oauth2AuthorizationRequest authorizationRequest) {
        return Flux.fromIterable(handlers)
                .flatMap(handler -> handler.supports(authorizationRequest))
                .filter(res -> res)
                .next()
                .defaultIfEmpty(false);
    }

    @Override
    @NotNull
    public Mono<Oauth2AuthorizationResponse<? extends Oauth2AuthorizationRequest>> permissionGranted(@NotNull final Oauth2AuthorizationRequest authorizationRequest,
                                                                                                     @NotNull final ResourceOwner resourceOwner) {
        return Flux.fromIterable(handlers)
                .flatMap(handler -> handler.supports(authorizationRequest)
                        .filter(it -> it)
                        .flatMap(res -> handler.permissionGranted(authorizationRequest, resourceOwner)))
                .next();
    }
}
