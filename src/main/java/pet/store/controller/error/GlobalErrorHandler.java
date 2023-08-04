package pet.store.controller.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoSuchElementException(NoSuchElementException ex) {
        log.info("Element not found: {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.toString());
        return errorResponse;
    }
}