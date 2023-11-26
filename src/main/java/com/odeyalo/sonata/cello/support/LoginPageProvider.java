package com.odeyalo.sonata.cello.support;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

/**
 * Provide the login page to return to the user as Flux of bytes
 */
public interface LoginPageProvider {

    @NotNull
    Flux<ByteBuffer> getLoginPage();

}
