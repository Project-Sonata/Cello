package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.client.Oauth2RegisteredClientAuthenticationAdapter;
import com.odeyalo.sonata.cello.core.client.registration.Oauth2RegisteredClientService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public final class Oauth2ClientAuthenticationFilter implements WebFilter {
    private final Oauth2RegisteredClientService registeredClientService;

    public Oauth2ClientAuthenticationFilter(final Oauth2RegisteredClientService registeredClientService) {
        this.registeredClientService = registeredClientService;
    }

    @Override
    @NotNull
    public Mono<Void> filter(@NotNull final ServerWebExchange exchange,
                             @NotNull final WebFilterChain chain) {

        if ( !exchange.getRequest().getPath().pathWithinApplication().toString().equals("/token") ) {
            return chain.filter(exchange);
        }

        return exchange.getFormData().flatMap(formData -> {
            final String clientId = formData.getFirst("client_id");
            final String clientSecret = formData.getFirst("client_secret");

            return registeredClientService.findByClientId(clientId)
                    .log()
                    .flatMap(client -> {
                        if ( !Objects.equals(client.getCredentials().getClientSecret(), clientSecret) ) {
                            return Mono.error(new IllegalArgumentException("can't authneticate the oauth2 client"));
                        }
                        return Mono.just(
                                new Oauth2RegisteredClientAuthenticationAdapter(client)
                        );
                    })
                    .map(ReactiveSecurityContextHolder::withAuthentication)
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("can't find a user: " + clientId)))
                    .log("set the authentication for oauth2 client")
                    .flatMap(authenticationContext -> chain.filter(exchange).contextWrite(authenticationContext));
        });
    }
}
