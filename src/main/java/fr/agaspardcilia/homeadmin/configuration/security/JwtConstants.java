package fr.agaspardcilia.homeadmin.configuration.security;

/**
 * JWT related constants.
 */
public class JwtConstants {
    /**
     * The ID field of JWT claims.
     */
    public static final String ID_KEY = "id";
    /**
     * The authority field of JWT claims.
     */
    public static final String AUTHORITIES_KEY = "auth";

    private JwtConstants() {
        // Do not instantiate! >:(
    }
}
