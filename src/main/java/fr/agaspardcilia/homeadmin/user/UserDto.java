package fr.agaspardcilia.homeadmin.user;

import java.util.UUID;

/**
 * A user.
 *
 * @param id the ID of the user.
 * @param mail the mail of the user.
 * @param isActive whether the user is activated or not.
 */
public record UserDto (
        UUID id,
        String mail,
        Boolean isActive,
        Boolean isAdmin
) { }
