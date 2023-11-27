package com.odeyalo.sonata.cello.web;

import com.odeyalo.sonata.cello.core.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
public class Oauth2Controller {

    @GetMapping(value = "/authorize")
    public Mono<String> handleAuthorize(AuthorizationRequest request) throws IOException {

        return Mono.just("login");
    }
}
