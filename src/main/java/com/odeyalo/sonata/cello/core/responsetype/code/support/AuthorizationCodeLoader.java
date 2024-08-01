package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeMetadata;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

public interface AuthorizationCodeLoader {

    /**
     * Load a metadata about authorization code using authorization code value
     *
     * @param authorizationCode - unique authorization code value
     * @return - {@link Mono} with {@link AuthorizationCodeMetadata} if code exists,
     * empty {@link Mono} if authorization code does not exist
     */
    @NotNull
    Mono<AuthorizationCodeMetadata> loadUsing(@NotNull String authorizationCode);

}
