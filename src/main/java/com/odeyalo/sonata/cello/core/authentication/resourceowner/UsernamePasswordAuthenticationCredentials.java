package com.odeyalo.sonata.cello.core.authentication.resourceowner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represent Username-password type of credentials
 */
@Value
@Builder
@AllArgsConstructor(staticName = "from")
public class UsernamePasswordAuthenticationCredentials implements AuthenticationCredentials {
    @NotNull
    String username;
    @NotNull
    String password;
}
