package com.odeyalo.sonata.cello.support.http;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * Used to wrap http request from different libraries(from Spring Webflux, Netty, etc)
 */
public interface ReactiveHttpRequest {

    URI getURI();

    Mono<Map<String, String>> getFormData();

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    Map<String, String> getQueryParams();

    String getMethod();

    HttpHeaders getHeaders();

    /**
     * @return immutable list of cookies
     */
    List<HttpCookie> getCookies();

    /**
     * @return - flux of byte buffers with body
     */
    Flux<ByteBuffer> getBody();

}