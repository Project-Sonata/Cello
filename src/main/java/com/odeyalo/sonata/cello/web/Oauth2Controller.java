package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import org.javaync.io.AsyncFiles;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_HTML;

@Controller
@RequestMapping
public class Oauth2Controller {

    @GetMapping(value = "/authorize")
    public Mono<ResponseEntity<?>> handleAuthorize(AuthorizationRequest request) throws IOException {
        Path path = new ClassPathResource("static/login-page.html").getFile().toPath();

        return Mono.fromFuture(AsyncFiles.readAllBytes(path))
                .map((bytes) -> ResponseEntity
                        .status(OK)
                        .contentType(TEXT_HTML)
                        .body(new String(bytes))
                );
    }
}
