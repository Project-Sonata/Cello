package com.odeyalo.sonata.cello.support;

import com.odeyalo.sonata.cello.support.io.ReactiveFileReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Provide the login page from provided path
 */
public class PathLoginPageLoader implements LoginPageLoader {
    private final String htmlPagePath;
    private final ReactiveFileReader reactiveFileReader;

    public PathLoginPageLoader(String htmlPagePath, ReactiveFileReader reactiveFileReader) {
        Assert.state(Files.exists(Path.of(htmlPagePath)), "Invalid path to html file was provided");
        this.reactiveFileReader = reactiveFileReader;
        this.htmlPagePath = htmlPagePath;
    }

    @Override
    @NotNull
    public Flux<ByteBuffer> getLoginPage() {
        return reactiveFileReader.readAllBytes(htmlPagePath);
    }
}
