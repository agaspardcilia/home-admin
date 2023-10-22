package fr.agaspardcilia.homeadmin.configuration;

import fr.agaspardcilia.homeadmin.common.exception.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Application controller advice. Will intercept exception and convert them to HTTP error responses.
 */
@RestControllerAdvice
public class ControllerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

    /**
     * Handle API exceptions.
     *
     * @param exception the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException exception) {
        LOGGER.debug("{}: {}", exception.getStatus(), exception.getMessage());

        return new ResponseEntity<>(
                new ApiExceptionResponse(exception.getStatus(), exception.getMessage()), exception.getStatus()
        );
    }

    /**
     * Handle validation exceptions.
     *
     * @param exception the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(e -> "'%s': %s".formatted(((FieldError)e).getField(), e.getDefaultMessage()))
                .collect(Collectors.joining("/n"));
        return new ResponseEntity<>(new ApiExceptionResponse(HttpStatus.BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    private record ApiExceptionResponse(
            String httpStatus,
            int httpStatusCode,
            String message,
            Instant timestamp
    ) {
        public ApiExceptionResponse(HttpStatus httpStatus, String message) {
            this(httpStatus.name(), httpStatus.value(), message, Instant.now());
        }
    }
}
