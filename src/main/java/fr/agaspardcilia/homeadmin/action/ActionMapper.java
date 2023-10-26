package fr.agaspardcilia.homeadmin.action;

import fr.agaspardcilia.homeadmin.action.dto.ActionDto;

/**
 * Maps things orbiting around {@link Action}s.
 */
public class ActionMapper {
    private ActionMapper() {
        // Do not instantiate! >:(
    }

    /**
     * Converts an action to a DTO.
     *
     * @param action the action to convert.
     * @return the DTO.
     */
    public static ActionDto toDto(Action action) {
        return new ActionDto(
                action.getId(),
                action.getName(),
                action.getRunnableExists(),
                action.getCreationDate(),
                action.getUpdateDate()
        );
    }
}
