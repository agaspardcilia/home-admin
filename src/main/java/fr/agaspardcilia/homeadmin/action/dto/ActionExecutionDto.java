package fr.agaspardcilia.homeadmin.action.dto;

/**
 * Contains the execution result of an action.
 *
 * @param outcome the outcome of the execution.
 * @param returnCode the return code of the execution.
 * @param stdin the stdin output of the execution.
 * @param stderr the stderr output of the execution.
 * @param exception the exception if something went wrong.
 */
public record ActionExecutionDto(
        ActionExecutionOutcome outcome,
        Integer returnCode,
        String stdin,
        String stderr,
        String exception
) { }
