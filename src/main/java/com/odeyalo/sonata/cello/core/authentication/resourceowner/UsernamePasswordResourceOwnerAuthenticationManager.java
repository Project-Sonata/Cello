package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.exception.ResourceOwnerAuthenticationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class UsernamePasswordResourceOwnerAuthenticationManager implements ResourceOwnerAuthenticationManager {
    private final ResourceOwnerService resourceOwnerService;
    private static final String USERNAME_FORM_DATA_KEY = "username";
    private static final String PASSWORD_FORM_DATA_KEY = "password";

    public UsernamePasswordResourceOwnerAuthenticationManager(ResourceOwnerService resourceOwnerService) {
        this.resourceOwnerService = resourceOwnerService;
    }

    @Override
    @NotNull
    public Mono<AuthenticatedResourceOwnerAuthentication> attemptAuthentication(@NotNull ServerWebExchange webExchange) {
        MediaType requestContentType = webExchange.getRequest().getHeaders().getContentType();

        if ( isNotMatchingRequest(requestContentType) ) {
            return Mono.empty();
        }

        return webExchange.getFormData()
                .flatMap(formData -> {
                    String username = formData.getFirst(USERNAME_FORM_DATA_KEY);
                    String password = formData.getFirst(PASSWORD_FORM_DATA_KEY);

                    if ( username == null || password == null ) {
                        return Mono.empty();
                    }

                    return authenticateUserOrError(username, password);
                });
    }

    private static boolean isNotMatchingRequest(MediaType requestContentType) {
        return !MediaType.APPLICATION_FORM_URLENCODED.equalsTypeAndSubtype(requestContentType)
                && !MediaType.MULTIPART_FORM_DATA.equalsTypeAndSubtype(requestContentType);
    }

    @NotNull
    private Mono<UsernamePasswordAuthenticatedResourceOwnerAuthentication> authenticateUserOrError(@NotNull String username,
                                                                                                   @NotNull String password) {
        return resourceOwnerService.loadResourceOwnerByUsername(username)
                .filter(resourceOwner -> isCredentialsValid(resourceOwner, password))
                .map(resourceOwner -> UsernamePasswordAuthenticatedResourceOwnerAuthentication.create(username, password, resourceOwner))
                .switchIfEmpty(Mono.defer(() -> Mono.error(
                        ResourceOwnerAuthenticationException.withCustomMessage("Username or password mismatch")
                )));
    }

    private static boolean isCredentialsValid(@NotNull ResourceOwner resourceOwner, @NotNull String password) {
        return Objects.equals(resourceOwner.getCredentials(), password);
    }
}