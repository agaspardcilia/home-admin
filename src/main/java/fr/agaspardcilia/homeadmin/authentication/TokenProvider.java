package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import fr.agaspardcilia.homeadmin.configuration.properties.Security;
import fr.agaspardcilia.homeadmin.configuration.security.JwtConstants;
import fr.agaspardcilia.homeadmin.user.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Builds JWT tokens.
 */
@Log4j2
@Component
public class TokenProvider {
    private final Duration tokenValidity;
    private final Duration tokenValidityForRememberMe;
    private final Key key;
    private final JwtParser jwtParser;

    /**
     * Constructor.
     *
     * @param appProperties the application configuration.
     */
    public TokenProvider(AppProperties appProperties) {
        Security.Jwt jwtProperties = appProperties.getSecurity().getJwt();

        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
        this.tokenValidity = Duration.of(jwtProperties.getTokenValidityInSeconds(), ChronoUnit.SECONDS);
        this.tokenValidityForRememberMe = Duration.of(jwtProperties.getTokenValidityInSecondsForRememberMe(), ChronoUnit.SECONDS);
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    /**
     * Creates a JWT token from a user authentication details.
     *
     * @param user the user to authenticate.
     * @param authentication the authentication of the user.
     * @param rememberMe whether the JWT validity should be long or not.
     * @return the JWT token.
     */
    public String createToken(UserDto user, Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Duration validityDuration = rememberMe ? tokenValidityForRememberMe : tokenValidity;
        Instant validity = Instant.now().plus(validityDuration);

        return Jwts.builder()
                .signWith(key)
                .setSubject(authentication.getName())
                .claim(JwtConstants.AUTHORITIES_KEY, authorities)
                .claim(JwtConstants.ID_KEY, user.id())
                .setExpiration(Date.from(validity))
                .compact();
    }

    /**
     * Get an authentication from a given JWT token.
     *
     * @param token the token.
     * @return the authentication if the token is valid.
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Set<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
        return new UsernamePasswordAuthenticationToken(claims.get(JwtConstants.ID_KEY), token, authorities);
    }

    /**
     * Validates a given JWT token signed by the server.
     *
     * @param token the token to validate.
     * @return {@code true} if valid, {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
