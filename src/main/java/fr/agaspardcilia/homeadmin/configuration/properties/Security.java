package fr.agaspardcilia.homeadmin.configuration.properties;

import lombok.Data;
import org.springframework.web.cors.CorsConfiguration;

/**
 * The application security configuration.
 */
@Data
public class Security {
    private Jwt jwt;
    private CorsConfiguration cors;

    @Data
    public static class Jwt {
        private String secret;
        private Long tokenValidityInSeconds;
        private Long tokenValidityInSecondsForRememberMe;
    }
}
