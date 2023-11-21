package com.odeyalo.sonata.cello;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureWebTestClient
class AuthorizeEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class ValidRequestTest {

        @Test
        void shouldReturnOkStatus() {
            WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                    .uri("/authorize")
                    .exchange();

            responseSpec.expectStatus().isOk();
        }

        @Test
        void shouldReturnHtmlContentType() {
            WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                    .uri("/authorize")
                    .exchange();

            responseSpec.expectHeader().contentType(MediaType.TEXT_HTML);
        }

        @Test
        void shouldReturnHtmlBody() {
            WebTestClient.ResponseSpec responseSpec = webTestClient.get()
                    .uri("/authorize")
                    .exchange();

            String htmlBody = responseSpec.expectBody(String.class).returnResult().getResponseBody();

            assertThat(htmlBody).isEqualTo("<p1>Hello from Cello server</p1>");
        }
    }


    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class MalformedRedirectUriRequestTest {

    }
}