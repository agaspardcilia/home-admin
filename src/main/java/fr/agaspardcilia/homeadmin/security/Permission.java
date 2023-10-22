package fr.agaspardcilia.homeadmin.security;

/**
 * Various available permissions.
 */
public enum Permission {
    USERS_GET,
    USERS_GET_ALL,
    USERS_REGISTER,
    USERS_CHANGE_PWD,

    // Articles
    FETCH_ADMIN_ONLY_ARTICLE,
    UPDATE_ARTICLE,
}
