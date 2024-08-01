package com.odeyalo.sonata.cello.support.view;

import lombok.SneakyThrows;
import org.javaync.io.AsyncFiles;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.reactive.result.view.UrlBasedViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Map;

/**
 * Resolve the HTML files from the classpath
 */
@Component
public class HtmlViewResolver extends UrlBasedViewResolver {

    public HtmlViewResolver() {
        super();
        setViewClass(HtmlView.class);
        setPrefix("classpath:public/");
        setSuffix(".html");
    }

    static class HtmlView extends AbstractUrlBasedView {

        @Override
        @SneakyThrows
        @NotNull
        protected Mono<Void> renderInternal(@NotNull final Map<String, Object> renderAttributes,
                                            @Nullable final MediaType contentType,
                                            @NotNull final ServerWebExchange exchange) {
            return Mono.fromFuture(AsyncFiles.readAllBytes(resolveView().getPath()))
                    .map(DefaultDataBufferFactory.sharedInstance::wrap)
                    .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)));
        }

        @Override
        public boolean checkResourceExists(@NotNull final Locale locale) throws Exception {
            return resolveView().exists();
        }

        @NotNull
        private File resolveView() throws FileNotFoundException {
            final String url = getUrl();

            if ( url == null ) {
                throw new FileNotFoundException("A file cannot be found for null URL");
            }

            return ResourceUtils.getFile(url);
        }
    }
}
