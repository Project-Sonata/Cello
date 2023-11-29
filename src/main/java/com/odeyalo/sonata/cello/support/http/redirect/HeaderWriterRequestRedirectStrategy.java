package com.odeyalo.sonata.cello.support.http.redirect;

import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Write the default headers used for redirect and set the 302 status code.
 */
@Component
public class HeaderWriterRequestRedirectStrategy implements RequestRedirectStrategy {

    @Override
    public Mono<Void> sendRedirect(@NotNull ReactiveHttpRequest request,
                                   @NotNull ReactiveHttpResponse response,
                                   @NotNull URI location) {

        return Mono.fromRunnable(() -> {
            Map<String, List<String>> headers = new HashMap<>(response.getHeaders().map());

            headers.put("Location", Collections.singletonList(location.toString()));

            response.setStatusCode(302);
            response.setHeaders(HttpHeaders.of(headers, ((s, s2) -> true)));
        });
    }
}