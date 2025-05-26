package io.tanibilet.server.shared;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler({ BindException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(Exception ex) {
        Map<String, Object> errors = new HashMap<>();
        if (ex instanceof BindException be) {
            be.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
        }
        return Map.of("errors", errors);
    }
}
