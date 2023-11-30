package com.odeyalo.sonata.cello.core.authentication;

import com.odeyalo.sonata.cello.core.authentication.support.FormDataResourceOwnerAuthenticationConverter;
import com.odeyalo.sonata.cello.support.http.*;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceOwnerAuthenticationFilterTest {

    @Test
    void shouldInvokeOnSuccessHandlerIfAuthIsSuccess() {
        var resourceOwner = AuthenticatedResourceOwner.of("odeyalo", "mikulover", null);
        AtomicBoolean invoked = new AtomicBoolean(false);

        ResourceOwnerAuthenticationFilter testable = new ResourceOwnerAuthenticationFilter(
                new FormDataResourceOwnerAuthenticationConverter(),
                preAuthentication -> Mono.just(resourceOwner),
                (request, response, authentication) -> Mono.fromRunnable(() -> invoked.set(true))
        );

        ReactiveHttpRequest request = MockReactiveHttpRequest.builder()
                .formValue("username", "odeyalo")
                .formValue("password", "mikulover")
                .build();

        ReactiveHttpResponse response = MockReactiveHttpResponse.empty();

        testable.doFilter(request, response)
                .as(StepVerifier::create)
                .verifyComplete();

        assertThat(invoked).isTrue();
    }
}