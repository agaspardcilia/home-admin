package fr.agaspardcilia.homeadmin.action;

/**
 * Thrown when attempting to access an unknown action.
 */
public class UnknownActionException extends Exception {
    public UnknownActionException(String message) {
        super(message);
    }
}
