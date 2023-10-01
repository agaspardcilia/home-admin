package fr.agaspardcilia.homeadmin.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class related to authentication.
 */
public class AuthenticationUtil {

    private AuthenticationUtil() {
        // Do not instantiate! >:(
    }

    /**
     * TODO: test me!
     * Retrieve the current user ID from the {@link SecurityContextHolder}.
     *
     * @return the ID if a user is connected, {@link Optional#empty()} otherwise.
     */
    public static Optional<UUID> getCurrentUserId() {
        String credentials = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (!StringUtils.isBlank(credentials)) {
            return Optional.of(UUID.fromString(credentials));
        } else {
            return Optional.empty();
        }
    }
}
