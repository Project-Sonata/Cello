package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.AuthorizationCodeRepository;
import com.odeyalo.sonata.cello.core.responsetype.code.AuthorizationCodeMetadata;
import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * {@link AuthorizationCodeService} that generates an opaque tokens and save them in {@link AuthorizationCodeRepository}
 */
public final class DefaultAuthorizationCodeService implements AuthorizationCodeService {
    private final Supplier<String> authorizationCodeValueGenerator;
    private final AuthorizationCodeRepository authorizationCodeRepository;

    public DefaultAuthorizationCodeService(final AuthorizationCodeRepository authorizationCodeRepository) {
        this.authorizationCodeRepository = authorizationCodeRepository;
        this.authorizationCodeValueGenerator = () -> RandomStringUtils.randomAlphanumeric(22);
    }

    public DefaultAuthorizationCodeService(final Supplier<String> authorizationCodeValueGenerator,
                                           final AuthorizationCodeRepository authorizationCodeRepository) {
        this.authorizationCodeValueGenerator = authorizationCodeValueGenerator;
        this.authorizationCodeRepository = authorizationCodeRepository;
    }

    @Override
    @NotNull
    public Mono<GeneratedAuthorizationCode> newAuthorizationCode(@NotNull final AuthorizationCodeGenerationContext generationContext) {

        final String authorizationCodeValue = authorizationCodeValueGenerator.get();

        final AuthorizationCodeMetadata codeMetadata = AuthorizationCodeMetadata.from(
                generationContext.getGrantedBy(),
                generationContext.getGrantedFor(),
                generationContext.getRequestedScopes(),
                generationContext.getClaims()
        );

        final GeneratedAuthorizationCode generatedAuthorizationCode = GeneratedAuthorizationCode.builder()
                .codeValue(authorizationCodeValue)
                .metadata(codeMetadata)
                .build();

        return authorizationCodeRepository.save(generatedAuthorizationCode)
                .thenReturn(generatedAuthorizationCode);
    }

    @Override
    @NotNull
    public Mono<AuthorizationCodeMetadata> loadUsing(@NotNull final String authorizationCode) {
        return authorizationCodeRepository.findByCodeValue(authorizationCode)
                .map(GeneratedAuthorizationCode::getMetadata);
    }
}
