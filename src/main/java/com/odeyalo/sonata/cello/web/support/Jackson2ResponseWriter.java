package com.odeyalo.sonata.cello.web.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Write HTTP body as JSON using {@link ObjectMapper}
 */
public final class Jackson2ResponseWriter implements ResponseWriter {
    private final ObjectMapper mapper;

    public Jackson2ResponseWriter(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public @NotNull Mono<Void> writeResponse(@NotNull final Object body, @NotNull final ServerWebExchange writeTo) {
        final DataBufferFactory bufferFactory = writeTo.getResponse().bufferFactory();

        writeTo.getResponse().getHeaders().setContentType(APPLICATION_JSON);
        return writeTo.getResponse().writeWith(
                Mono.just(bufferFactory.wrap(asJson(body)))
        );
    }

    private byte[] asJson(final Object body) {
        try {
            return mapper.writeValueAsBytes(body);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
