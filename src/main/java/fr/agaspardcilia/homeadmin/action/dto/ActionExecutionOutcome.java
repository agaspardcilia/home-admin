package fr.agaspardcilia.homeadmin.action.dto;

/**
 * The execution outcome of an action.
 */
public enum ActionExecutionOutcome {
    /**
     * The execution was a success.
     */
    SUCCESS,
    /**
     * The execution was a failure.
     */
    FAILURE,
    /**
     * The execution timed out.
     */
    TIMEOUT,
    /**
     * An exception was thrown during the execution.
     */
    EXCEPTION,
    /**
     * Dunno mate ¯\_(ツ)_/¯
     */
    UNKNOWN

}
