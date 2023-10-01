package fr.agaspardcilia.homeadmin.authentication.exception;

/**
 * Throw when an authentication attempt fails with bad credentiols.
 */
public class InvalidCredentialException extends Exception {
    public InvalidCredentialException(String message) {
        super(message);
    }
}
