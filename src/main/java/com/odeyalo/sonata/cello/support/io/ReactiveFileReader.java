package com.odeyalo.sonata.cello.support.io;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;

/**
 * Reads the file reactively, return flux of the bytes
 */
public interface ReactiveFileReader {

    @NotNull
    Flux<ByteBuffer> readAllBytes(@NotNull String path);

    @NotNull
    Flux<ByteBuffer> readAllBytes(@NotNull String path, int bufferSize);

}
