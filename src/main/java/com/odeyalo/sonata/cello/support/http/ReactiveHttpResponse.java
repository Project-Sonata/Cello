package com.odeyalo.sonata.cello.support.http;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.net.http.HttpHeaders;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

/**
 * Represent a reactive Http response
 */
public interface ReactiveHttpResponse {

    /**
     * Set the HTTP status code of the response.
     * @param status the HTTP status code
     * @return {@code false} if the status code change wasn't processed because
     * the HTTP response is committed, {@code true} if successfully set.
     */
    boolean setStatusCode(Integer status);

    /**
     * Return the status code that has been set, or otherwise fall back on the
     * status of the response from the underlying server. The return value may
     * be {@code null} if there is no default value from the
     * underlying server.
     */
    Integer getStatusCode();

    /**
     * @return the headers of this response, never null
     */
    HttpHeaders getHeaders();

    /**
     * set the headers for this response
     */
    void setHeaders(HttpHeaders headers);

    /**
     * Register an action to apply just before the HttpOutputMessage is committed.
     * <p><strong>Note:</strong> the supplied action must be properly deferred,
     * e.g. via {@link Mono#defer} or {@link Mono#fromRunnable}, to ensure it's
     * executed in the right order, relative to other actions.
     *
     * @param action the action to apply
     */
    void beforeCommit(Supplier<? extends Mono<Void>> action);

    /**
     * Whether the HttpOutputMessage is committed.
     */
    boolean isCommitted();

    /**
     * Use the given {@link Publisher} to write the body of the message to the
     * underlying HTTP layer.
     *
     * @param body the body content publisher
     * @return a {@link Mono} that indicates completion or error
     */

    Mono<Void> writeWith(Publisher<? extends ByteBuffer> body);

    /**
     * Indicate that message handling is complete, allowing for any cleanup or
     * end-of-processing tasks to be performed such as applying header changes
     * made via {@link #getHeaders()} to the underlying HTTP message (if not
     * applied already).
     * <p>This method should be automatically invoked at the end of message
     * processing so typically applications should not have to invoke it.
     * If invoked multiple times it should have no side effects.
     *
     * @return a {@link Mono} that indicates completion or error
     */
    Mono<Void> setComplete();
}
