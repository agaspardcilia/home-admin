package fr.agaspardcilia.homeadmin.security;

import lombok.Getter;

import java.util.Set;

/**
 * Represents authority groups and what they are allowed to do.
 */
@Getter
public enum Authority {
    ADMIN(
            // Users
            Permission.USERS_GET, Permission.USERS_REGISTER, Permission.USERS_CHANGE_PWD, Permission.USERS_GET_ALL,
            // Articles
            Permission.FETCH_ADMIN_ONLY_ARTICLE
    ),
    USER(),
    ROLE_ANONYMOUS();

    private final Set<Permission> permissions;

    Authority(Permission... permissions) {
        this.permissions = Set.of(permissions);
    }
}
