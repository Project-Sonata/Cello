package com.odeyalo.sonata.cello.core.authentication.support;

import com.odeyalo.sonata.cello.core.authentication.ResourceOwnerPreAuthentication;
import com.odeyalo.sonata.cello.core.authentication.UsernamePasswordResourceOwnerPreAuthentication;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Convert the {@link ReactiveHttpRequest} to {@link UsernamePasswordResourceOwnerPreAuthentication}
 */
@Component
public class FormDataResourceOwnerAuthenticationConverter implements ResourceOwnerAuthenticationConverter {
    private String usernameParameter = "username";
    private String passwordParameter = "password";

    @Override
    public Mono<ResourceOwnerPreAuthentication> convert(ReactiveHttpRequest httpRequest) {
        return httpRequest.getFormData()
                .flatMap(FormDataResourceOwnerAuthenticationConverter::validateRequest)
                .map(this::createAuthentication);
    }

    @NotNull
    private static Mono<Map<String, String>> validateRequest(Map<String, String> formData) {
        if ( !formData.containsKey("username") || !formData.containsKey("password") ) {
            return Mono.empty();
        }
        return Mono.just(formData);
    }

    @NotNull
    private UsernamePasswordResourceOwnerPreAuthentication createAuthentication(Map<String, String> formData) {
        String username = formData.get(usernameParameter);
        String password = formData.get(passwordParameter);
        return UsernamePasswordResourceOwnerPreAuthentication.withCredentials(username, password);
    }

    public void setUsernameParameter(@NotNull String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public void setPasswordParameter(@NotNull String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }
}
