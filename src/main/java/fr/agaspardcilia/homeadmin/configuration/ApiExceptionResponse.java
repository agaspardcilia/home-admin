package fr.agaspardcilia.homeadmin.configuration;

import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * An API exception response.
 *
 * @param httpStatus the HTTP status text of the response.
 * @param httpStatusCode the HTTP status code of the response.
 * @param message the message of the response.
 * @param timestamp the timestamp of the response.
 */
public record ApiExceptionResponse(
        String httpStatus,
        int httpStatusCode,
        String message,
        Instant timestamp
) {
    public ApiExceptionResponse(HttpStatus httpStatus, String message) {
        this(httpStatus.name(), httpStatus.value(), message, Instant.now());
    }
}
