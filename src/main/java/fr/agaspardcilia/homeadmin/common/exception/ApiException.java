package fr.agaspardcilia.homeadmin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API exceptions, should be converted to HTTP responses when thrown.
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    /**
     * Constructor.
     *
     * @param status the status of the error.
     * @param message the message of the error.
     */
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
