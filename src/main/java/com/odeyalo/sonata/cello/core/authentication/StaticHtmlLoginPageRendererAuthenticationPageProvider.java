package com.odeyalo.sonata.cello.core.authentication;

import com.odeyalo.sonata.cello.support.io.ReactiveFileReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class StaticHtmlLoginPageRendererAuthenticationPageProvider implements AuthenticationPageProvider {
    private final ReactiveFileReader reactiveFileReader;

    public StaticHtmlLoginPageRendererAuthenticationPageProvider(ReactiveFileReader reactiveFileReader) {
        this.reactiveFileReader = reactiveFileReader;
    }

    @Override
    @NotNull
    public Mono<ServerHttpResponse> getAuthenticationPage(@NotNull ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.TEXT_HTML);

        return response.writeWith(Mono.from(reactiveFileReader.readAllBytes("C:\\Users\\thepr\\IdeaProjects\\Sonata\\Cello\\src\\main\\resources\\public\\login.html"))
                        .map(byteBuffer -> response.bufferFactory().wrap(byteBuffer)))
                .thenReturn(response);
    }
}
