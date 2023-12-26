package com.odeyalo.sonata.cello.exception;

import com.odeyalo.sonata.cello.core.Oauth2ErrorCode;
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
        if ( ex.getError() == Oauth2ErrorCode.INVALID_CLIENT ) {
            return ResponseEntity.badRequest().body("The client is invalid!");
        }
        return ResponseEntity.badRequest().body("not  handled now. TODO");
    }


    @ExceptionHandler(UnacceptableOauth2RedirectUriException.class)
    public ResponseEntity<?> handleUnacceptableOauth2RedirectUriException(UnacceptableOauth2RedirectUriException ex) {
        return ResponseEntity.badRequest().body("Redirect uri is not authorized, so we can't redirect you! :)");
    }


}
