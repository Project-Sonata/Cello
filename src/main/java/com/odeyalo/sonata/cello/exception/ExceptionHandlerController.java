package com.odeyalo.sonata.cello.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handle exceptions occurred in Cello
 */
@RestControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(Oauth2AuthorizationRequestValidationException.class)
    public ResponseEntity<?> handleOauth2AuthorizationRequestValidationException(Oauth2AuthorizationRequestValidationException ex) {
        return ResponseEntity.badRequest().body("The client is invalid!");
    }
}
