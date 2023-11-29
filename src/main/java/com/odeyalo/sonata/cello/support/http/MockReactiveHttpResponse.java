package com.odeyalo.sonata.cello.support.http;

import lombok.Builder;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Mock impl of ReactiveHttpResponse
 */
@Builder
public class MockReactiveHttpResponse implements ReactiveHttpResponse {
    private boolean isCommitted;
    private Integer status;
    @Builder.Default
    private HttpHeaders headers = HttpHeaders.of(Collections.emptyMap(), ((s, s2) -> true));
    private Supplier<? extends Mono<Void>> beforeCommitAction;

    public static MockReactiveHttpResponse empty() {
        return builder().build();
    }

    @Override
    public boolean setStatusCode(Integer status) {
        if ( isCommitted ) {
            return false;
        }
        this.status = status;
        return true;
    }

    @Override
    public Integer getStatusCode() {
        return status;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        this.beforeCommitAction = action;
    }

    @Override
    public boolean isCommitted() {
        return isCommitted;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends ByteBuffer> body) {
        return null;
    }

    @Override
    public Mono<Void> setComplete() {
        return Mono.defer(beforeCommitAction)
                .then(Mono.fromRunnable(() -> isCommitted = true));
    }
}
