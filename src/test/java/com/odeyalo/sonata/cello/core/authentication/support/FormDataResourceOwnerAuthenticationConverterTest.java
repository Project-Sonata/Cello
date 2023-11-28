package com.odeyalo.sonata.cello.core.authentication.support;

import com.odeyalo.sonata.cello.core.authentication.UsernamePasswordResourceOwnerPreAuthentication;
import com.odeyalo.sonata.cello.support.http.MockReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.ReactiveHttpRequest;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FormDataResourceOwnerAuthenticationConverterTest {

    @Test
    void shouldConvertAndReturnAuthenticationOnValidRequest() {
        ReactiveHttpRequest request = MockReactiveHttpRequest.builder()
                .formValue("username", "odeyalo")
                .formValue("password", "mikulover")
                .build();

        FormDataResourceOwnerAuthenticationConverter testable = new FormDataResourceOwnerAuthenticationConverter();

        testable.convert(request)
                .as(StepVerifier::create)
                .expectNext(UsernamePasswordResourceOwnerPreAuthentication.withCredentials("odeyalo", "mikulover"))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfParametersNotPresent() {
        ReactiveHttpRequest request = MockReactiveHttpRequest.empty();

        FormDataResourceOwnerAuthenticationConverter testable = new FormDataResourceOwnerAuthenticationConverter();

        testable.convert(request)
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyIfPasswordIsMissingNotPresent() {
        ReactiveHttpRequest request = MockReactiveHttpRequest.builder()
                .formValue("username", "odeyalo")
                .build();

        FormDataResourceOwnerAuthenticationConverter testable = new FormDataResourceOwnerAuthenticationConverter();

        testable.convert(request)
                .as(StepVerifier::create)
                .verifyComplete();
    }
    @Test
    void shouldReturnEmptyIfUsernameIsMissingNotPresent() {
        ReactiveHttpRequest request = MockReactiveHttpRequest.builder()
                .formValue("password", "mikulover")
                .build();

        FormDataResourceOwnerAuthenticationConverter testable = new FormDataResourceOwnerAuthenticationConverter();

        testable.convert(request)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}