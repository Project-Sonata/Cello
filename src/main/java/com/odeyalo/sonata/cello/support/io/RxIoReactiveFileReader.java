package com.odeyalo.sonata.cello.support.io;

import org.javaync.io.AsyncFiles;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * Uses RxIO library to read file reactively
 */
public class RxIoReactiveFileReader implements ReactiveFileReader {
    private static final int ONE_MEGABYTE = 1024 * 1024;

    @Override
    @NotNull
    public Flux<ByteBuffer> readAllBytes(@NotNull String path) {
        return readAllBytes(path, ONE_MEGABYTE);
    }

    @Override
    @NotNull
    public Flux<ByteBuffer> readAllBytes(@NotNull String path, int bufferSize) {
        return Mono.fromFuture(AsyncFiles.readAllBytes(Path.of(path), bufferSize))
                .map(ByteBuffer::wrap)
                .as(Flux::from);
    }
}
