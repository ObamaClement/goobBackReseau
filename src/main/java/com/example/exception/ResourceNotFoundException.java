package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception personnalisée levée lorsqu'une ressource demandée n'est pas trouvée.
 * L'annotation @ResponseStatus indique à Spring de retourner automatiquement
 * un code HTTP 404 NOT FOUND lorsque cette exception est levée par un controller.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}