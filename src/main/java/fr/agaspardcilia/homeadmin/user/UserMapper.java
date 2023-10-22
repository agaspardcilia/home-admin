package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.security.Authority;
import fr.agaspardcilia.homeadmin.user.entity.User;

/**
 * Mapper centered around {@link User}s.
 */
public class UserMapper {

    private UserMapper() {
        // Do not instantiate! >:(
    }

    /**
     * Creates a new instance from a given {@link User}.
     *
     * @param user the user.
     * @return the new instance.
     */
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getMail(),
                user.getIsActive(),
                user.getAuthorities().contains(Authority.ADMIN)
        );
    }
}
