package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolves the {@link UsernamePasswordAuthenticationCredentials} from the given {@link ServerWebExchange#getFormData()}
 * <p>
 * Rules applied:
 * <p>
 * if {@link #usernameFormDataKey} is not present in form data - empty {@link Mono} is returned
 * if {@link #passwordFormDataKey} is not present in form data - empty {@link Mono} is returned
 *
 * if both are present and not null, then {@link UsernamePasswordAuthenticationCredentials} is returned as result
 */
@Setter
public final class FormDataUsernamePasswordAuthenticationCredentialsConverter implements AuthenticationCredentialsConverter {
    public static final String USERNAME_FORM_DATA_KEY = "username";
    public static final String PASSWORD_FORM_DATA_KEY = "password";

    private String usernameFormDataKey = USERNAME_FORM_DATA_KEY;
    private String passwordFormDataKey = PASSWORD_FORM_DATA_KEY;

    @Override
    @NotNull
    public Mono<AuthenticationCredentials> convertToCredentials(@NotNull ServerWebExchange webExchange) {
        return webExchange.getFormData()
                .mapNotNull(formData -> maybeConvert(formData));
    }

    @Nullable
    private UsernamePasswordAuthenticationCredentials maybeConvert(MultiValueMap<String, String> formData) {
        String username = formData.getFirst(usernameFormDataKey);
        String password = formData.getFirst(passwordFormDataKey);

        if ( username == null || password == null ) {
            return null;
        }

        return UsernamePasswordAuthenticationCredentials.from(username, password);
    }
}
