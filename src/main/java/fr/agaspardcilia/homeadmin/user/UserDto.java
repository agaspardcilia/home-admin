package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.user.entity.User;

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
        Boolean isActive
) {
    /**
     * TODO: test me!
     * Creates a new instance from a given {@link User}.
     *
     * @param user the user.
     * @return the new instance.
     */
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getMail(), user.getIsActive());
    }
}
