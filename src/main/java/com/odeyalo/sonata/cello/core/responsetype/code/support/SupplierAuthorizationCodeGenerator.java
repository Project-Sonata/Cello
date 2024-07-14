package com.odeyalo.sonata.cello.core.responsetype.code.support;

import com.odeyalo.sonata.cello.core.responsetype.code.GeneratedAuthorizationCode;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Mock implementation that returns the result from supplier
 */
public final class SupplierAuthorizationCodeGenerator implements AuthorizationCodeGenerator {
    private final Supplier<String> codeSupplier;

    public SupplierAuthorizationCodeGenerator(final Supplier<String> codeSupplier) {
        this.codeSupplier = codeSupplier;
    }

    @Override
    @NotNull
    public Mono<GeneratedAuthorizationCode> newAuthorizationCode() {
        return Mono.fromSupplier(codeSupplier)
                .map(GeneratedAuthorizationCode::wrapString);
    }
}
