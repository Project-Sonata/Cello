package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.ScopeContainer;
import com.odeyalo.sonata.cello.core.SimpleScope;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class UsernamePasswordResourceOwnerAuthenticationManager implements ResourceOwnerAuthenticationManager {
    private final ResourceOwnerService resourceOwnerService;

    public UsernamePasswordResourceOwnerAuthenticationManager(ResourceOwnerService resourceOwnerService) {
        this.resourceOwnerService = resourceOwnerService;
    }

    @Override
    public Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(ServerWebExchange webExchange) {
        MediaType requestContentType = webExchange.getRequest().getHeaders().getContentType();

        if ( !Objects.equals(requestContentType, MediaType.APPLICATION_FORM_URLENCODED) &&
                !Objects.equals(requestContentType, MediaType.MULTIPART_FORM_DATA) ) {
            return Mono.empty();
        }

        return webExchange.getFormData()
                .filter(formData -> formData.getFirst("username") != null && formData.getFirst("password") != null)
                .flatMap(formData -> {
                    String username = formData.getFirst("username");
                    String password = formData.getFirst("password");

                    return resourceOwnerService.loadResourceOwnerByUsername(username)
                            .flatMap(resourceOwner -> {
                                if ( Objects.equals(resourceOwner.getCredentials(), password) ) {
                                    return Mono.just(
                                            new UsernamePasswordAuthenticatedResourceOwnerAuthentication(username, password, resourceOwner)
                                    );
                                }
                                return Mono.empty();

                            }).switchIfEmpty(Mono.error(ResourceOwnerAuthenticationException.withCustomMessage("Username or password mismatch")));
                });

    }
}
