package com.odeyalo.sonata.cello.support.http;

import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapt the {@link ServerWebExchange} to ReactiveHttpReqyest
 */
public class ServerWebExchangeReactiveHttpRequest implements ReactiveHttpRequest {
    private final ServerWebExchange webExchange;

    public ServerWebExchangeReactiveHttpRequest(ServerWebExchange webExchange) {
        this.webExchange = webExchange;
    }

    @Override
    public URI getURI() {
        return webExchange.getRequest().getURI();
    }

    @Override
    public Mono<Map<String, String>> getFormData() {
        return webExchange.getFormData()
                .map(MultiValueMap::toSingleValueMap);
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return webExchange.getRequest().getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return webExchange.getRequest().getRemoteAddress();
    }

    @Override
    public Map<String, String> getQueryParams() {
        return webExchange.getRequest().getQueryParams().toSingleValueMap();
    }

    @Override
    public String getMethod() {
        return webExchange.getRequest().getMethod().name();
    }

    @Override
    public HttpHeaders getHeaders() {
        Set<Map.Entry<String, List<String>>> entries = webExchange.getRequest().getHeaders().entrySet();

        Map<String, List<String>> headersMap = entries.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return HttpHeaders.of(headersMap, (header1, header2) -> true);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return webExchange.getRequest().getCookies().toSingleValueMap().values()
                .stream()
                .map(cookie -> new HttpCookie(cookie.getName(), cookie.getValue()))
                .toList();
    }

    @Override
    public Flux<ByteBuffer> getBody() {
        return webExchange.getRequest().getBody()
                .map(dataBuffer -> {
                    // we get data buffers in chucks anyway, no need to split them in smaller one
                    ByteBuffer dest = ByteBuffer.allocate(dataBuffer.readableByteCount());
                    dataBuffer.toByteBuffer(dest);
                    return dest;
                });
    }
}
