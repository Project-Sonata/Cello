package testing.factory;

import com.odeyalo.sonata.cello.core.authentication.resourceowner.AuthenticationCredentialsConverter;
import com.odeyalo.sonata.cello.core.authentication.resourceowner.FormDataUsernamePasswordAuthenticationCredentialsConverter;

/**
 * Factory to create different {@link AuthenticationCredentialsConverter}
 */
public final class AuthenticationCredentialsConverters {

    public static AuthenticationCredentialsConverter usernamePassword() {
        return new FormDataUsernamePasswordAuthenticationCredentialsConverter();
    }
}
