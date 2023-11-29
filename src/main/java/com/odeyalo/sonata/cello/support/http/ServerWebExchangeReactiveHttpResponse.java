package com.odeyalo.sonata.cello.support.http;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Wrap the {@link ServerWebExchange} to {@link ReactiveHttpResponse}
 */
public class ServerWebExchangeReactiveHttpResponse implements ReactiveHttpResponse {
    private final ServerWebExchange serverWebExchange;

    public ServerWebExchangeReactiveHttpResponse(ServerWebExchange serverWebExchange) {
        this.serverWebExchange = serverWebExchange;
    }

    @Override
    public boolean setStatusCode(Integer status) {
        return serverWebExchange.getResponse().setRawStatusCode(status);
    }

    @Override
    public Integer getStatusCode() {
        return serverWebExchange.getResponse().getStatusCode().value();
    }

    @Override
    public HttpHeaders getHeaders() {
        Set<Map.Entry<String, List<String>>> entries = serverWebExchange.getRequest().getHeaders().entrySet();

        Map<String, List<String>> headersMap = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return HttpHeaders.of(headersMap, (header1, header2) -> true);
    }

    @Override
    public void setHeaders(HttpHeaders headers) {
        serverWebExchange.getResponse().getHeaders().clear();

        headers.map().forEach((key, value) -> serverWebExchange.getResponse().getHeaders().addAll(key, value));
    }

    @Override
    public void beforeCommit(Supplier<? extends Mono<Void>> action) {
        serverWebExchange.getResponse().beforeCommit(action);
    }

    @Override
    public boolean isCommitted() {
        return serverWebExchange.getResponse().isCommitted();
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends ByteBuffer> body) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        Publisher<DataBuffer> dataBufferWriter;

        if ( body instanceof Flux ) {
            @SuppressWarnings("unchecked")
            Flux<? extends ByteBuffer> fluxBody = (Flux<? extends ByteBuffer>) body;

            dataBufferWriter = fluxBody.map(bytes -> response.bufferFactory().wrap(bytes));
        } else {
            dataBufferWriter = ((Mono<? extends ByteBuffer>) body).map(bytes -> response.bufferFactory().wrap(bytes));
        }
        return response.writeWith(dataBufferWriter);
    }

    @Override
    public Mono<Void> setComplete() {
        return serverWebExchange.getResponse().setComplete();
    }
}
