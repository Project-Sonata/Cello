package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Generate a new {@link GeneratedAuthorizationCode} with opaque authorization code value as result.
 */
@Component
public final class DefaultAuthorizationCodeGenerator implements AuthorizationCodeGenerator {
    private final Supplier<String> authorizationCodeValueGenerator;

    public DefaultAuthorizationCodeGenerator() {
        this.authorizationCodeValueGenerator = () -> RandomStringUtils.randomAlphanumeric(22);
    }

    public DefaultAuthorizationCodeGenerator(final Supplier<String> authorizationCodeValueGenerator) {
        this.authorizationCodeValueGenerator = authorizationCodeValueGenerator;
    }

    @Override
    @NotNull
    public Mono<GeneratedAuthorizationCode> newAuthorizationCode(@NotNull final AuthorizationCodeGenerationContext generationContext) {

        final String authorizationCodeValue = authorizationCodeValueGenerator.get();

        return Mono.just(
                GeneratedAuthorizationCode.builder()
                        .codeValue(authorizationCodeValue)
                        .scopes(generationContext.getRequestedScopes())
                        .grantedFor(generationContext.getGrantedFor())
                        .grantedBy(generationContext.getGrantedBy())
                        .build()
        );
    }
}
