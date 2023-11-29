package com.odeyalo.sonata.cello.support.http.redirect;

import com.odeyalo.sonata.cello.support.http.MockReactiveHttpRequest;
import com.odeyalo.sonata.cello.support.http.MockReactiveHttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HeaderWriterRequestRedirectStrategyTest {

    @Test
    void shouldAddTheLocationUrlToResponse() {
        var testable = new HeaderWriterRequestRedirectStrategy();
        MockReactiveHttpResponse response = MockReactiveHttpResponse.empty();
        String location = "http://localhost:3000/hello";

        Void unused = testable.sendRedirect(MockReactiveHttpRequest.empty(), response, URI.create(location)).block();

        assertThat(response.getHeaders().firstValue("Location"))
                .hasValue(location);
    }

    @Test
    void shouldReturn302StatusCode() {
        var testable = new HeaderWriterRequestRedirectStrategy();
        MockReactiveHttpResponse response = MockReactiveHttpResponse.empty();
        String location = "http://localhost:3000/hello";

        Void unused = testable.sendRedirect(MockReactiveHttpRequest.empty(), response, URI.create(location)).block();

        assertThat(response.getStatusCode()).isEqualTo(302);
    }
}