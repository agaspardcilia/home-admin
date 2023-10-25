package fr.agaspardcilia.homeadmin.action.dto;

import fr.agaspardcilia.homeadmin.action.Action;

import java.time.Instant;
import java.util.UUID;

/**
 * An {@link Action} DTO.
 *
 * @param id the id of the action.
 * @param name the name of the action.
 * @param runnableExists whether the actual runnable file exists.
 * @param creationDate the creation date of the entity.
 * @param updateDate the last update date of the entity.
 */
public record ActionDto(
        UUID id,
        String name,
        boolean runnableExists,
        Instant creationDate,
        Instant updateDate
) { }
