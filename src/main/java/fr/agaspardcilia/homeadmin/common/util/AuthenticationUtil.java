package fr.agaspardcilia.homeadmin.common.util;

import fr.agaspardcilia.homeadmin.security.Authority;
import fr.agaspardcilia.homeadmin.security.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

/**
 * Utility class related to authentication.
 */
public class AuthenticationUtil {

    private AuthenticationUtil() {
        // Do not instantiate! >:(
    }

    /**
     * Retrieve the current user ID from the {@link SecurityContextHolder}.
     *
     * @return the ID if a user is connected, {@link Optional#empty()} otherwise.
     */
    public static Optional<UUID> getCurrentUserId() {
        String credentials = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!StringUtils.isBlank(credentials)) {
            return Optional.of(UUID.fromString(credentials));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Fetches current user's permissions.
     *
     * @return the {@link Set} of permissions.
     */
    public static Set<Permission> getCurrentUserPermissions() {
        Collection<? extends GrantedAuthority> rawAuthorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities();

        Set<Permission> result = new HashSet<>();
        for (GrantedAuthority rawAuthority : rawAuthorities) {
            try {
                result.addAll(Authority.valueOf(rawAuthority.getAuthority()).getPermissions());
            } catch (IllegalArgumentException e) {
                // Ignore, value is not part or recognised Authorities.
            }
        }
        return Set.copyOf(result);
    }
}
