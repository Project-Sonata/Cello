package com.odeyalo.sonata.cello.support.view;

import lombok.SneakyThrows;
import org.javaync.io.AsyncFiles;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.result.view.AbstractUrlBasedView;
import org.springframework.web.reactive.result.view.UrlBasedViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
        protected Mono<Void> renderInternal(Map<String, Object> renderAttributes, MediaType contentType, ServerWebExchange exchange) {
            return Mono.fromFuture(AsyncFiles.readAllBytes(ResourceUtils.getFile(getUrl()).getPath()))
                    .map(DefaultDataBufferFactory.sharedInstance::wrap)
                    .flatMap(buffer -> exchange.getResponse().writeWith(Mono.just(buffer)));
        }

        @Override
        public boolean checkResourceExists(Locale locale) throws Exception {
            return ResourceUtils.getFile(getUrl()).exists();
        }
    }
}
