package fr.agaspardcilia.homeadmin.configuration;

import fr.agaspardcilia.homeadmin.common.exception.UnknownEntityException;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Application controller advice. Will intercept exception and convert them to HTTP error responses.
 */
@RestControllerAdvice
@Log4j2
public class ControllerAdvice {

    /**
     * Handle API exceptions.
     *
     * @param exception the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException exception) {
        log.debug("{}: {}", exception.getStatus(), exception.getMessage());

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

    /**
     * Handles unknown entity exceptions.
     *
     * @param exception the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleUnknownEntityException(UnknownEntityException exception) {
        return new ResponseEntity<>(
                new ApiExceptionResponse(HttpStatus.NOT_FOUND, exception.getMessage()), HttpStatus.NOT_FOUND
        );
    }

    /**
     * Handles invalid media type exception.
     *
     * @param e the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(
                new ApiExceptionResponse(HttpStatus.BAD_REQUEST, "Media type not supported"),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Handles generic exceptions.
     *
     * @param e the exception.
     * @return the error.
     */
    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception e) {
        log.error("Unhandled Exception", e);
        return new ResponseEntity<>(
                new ApiExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled exception"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
