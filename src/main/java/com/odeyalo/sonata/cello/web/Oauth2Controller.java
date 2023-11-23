package com.odeyalo.sonata.cello.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_HTML;

@RestController
@RequestMapping
public class Oauth2Controller {

    @GetMapping(value = "/authorize")
    public Mono<ResponseEntity<?>> handleAuthorize(@RequestParam("response_type") String responseType,
                                                   @RequestParam("client_id") String clientId,
                                                   @RequestParam("redirect_uri") String redirectUri,
                                                   @RequestParam("scope") String scope,
                                                   @RequestParam("state") String state) {
        return Mono.just(ResponseEntity
                .status(OK)
                .contentType(TEXT_HTML)
                .body("<p1>Hello from Cello server</p1>"));
    }
}
