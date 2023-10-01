package fr.agaspardcilia.homeadmin.security;

/**
 * Various available permissions.
 * TODO: remove unused ones.
 */
public enum Permission {
    USERS_GET,
    USERS_GET_ALL,
    USERS_REGISTER,
    USERS_REQUEST_PWD_RESET,
    USERS_RESET_PWD,
    USERS_CHANGE_PWD,
    USERS_ACTIVATE,
    USERS_GET_SELF,
    AUTHENTICATION_JWT_AUTH,
}
