package com.odeyalo.sonata.cello.support.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@Value
@AllArgsConstructor(staticName = "of")
@Builder
public class MockReactiveHttpRequest implements ReactiveHttpRequest {
    URI uri;
    @Singular(value = "formValue")
    Map<String, String> formData;
    InetSocketAddress localAddress;
    InetSocketAddress remoteAddress;
    @Singular
    Map<String, String> queryParams;
    String httpMethod;
    HttpHeaders httpHeaders;
    List<HttpCookie> cookies;
    Flux<ByteBuffer> body;

    public static ReactiveHttpRequest empty() {
        return builder().build();
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public Mono<Map<String, String>> getFormData() {
        return Mono.just(formData);
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String getMethod() {
        return httpMethod;
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return cookies;
    }

    @Override
    public Flux<ByteBuffer> getBody() {
        return body;
    }
}
