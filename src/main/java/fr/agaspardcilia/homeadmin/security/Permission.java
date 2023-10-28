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
    ARTICLES_FETCH_ADMIN_ONLY,
    ARTICLES_UPDATE,

    //Actions
    ACTIONS_SCAN_DIRECTORY,
    ACTIONS_GET,
    ACTIONS_RUN,
    ACTIONS_RENAME
}
