package fr.agaspardcilia.homeadmin.user;

import fr.agaspardcilia.homeadmin.common.annotation.PermissionRequired;
import fr.agaspardcilia.homeadmin.common.annotation.PermitAll;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiForbiddenException;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiNotFoundException;
import fr.agaspardcilia.homeadmin.security.Permission;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * User controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Get a user by its ID.
     *
     * @param id the ID of the user.
     * @return the user.
     * @throws ApiNotFoundException if the user can't be found.
     */
    @GetMapping("/{id}")
    @PermissionRequired(Permission.USERS_GET)
    public UserDto get(@PathVariable UUID id) {
        return userService.get(id)
                .orElseThrow(() -> new ApiNotFoundException("Cannot find user"));
    }

    /**
     * @return the currently authenticated user.
     * @throws ApiForbiddenException if not user is connected.
     */
    @GetMapping("/current")
    @PermitAll
    public UserDto get() {
        return userService.getCurrentUser()
                .orElseThrow(() -> new ApiForbiddenException("No authenticated user"));
    }
}
