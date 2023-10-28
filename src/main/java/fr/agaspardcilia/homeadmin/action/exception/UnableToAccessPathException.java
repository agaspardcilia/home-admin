package fr.agaspardcilia.homeadmin.action.exception;

/**
 * Throw when a path cannot be accessed.
 */
public class UnableToAccessPathException extends Exception {
    public UnableToAccessPathException(String message) {
        super(message);
    }
}
