package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.authentication.exception.InvalidTokenException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class TokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

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
     * TODO: test me!
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
                .setSubject(authentication.getName())
                .claim(JwtConstants.AUTHORITIES_KEY, authorities)
                .claim(JwtConstants.ID_KEY, user.id())
                .setExpiration(Date.from(validity))
                .compact();
    }

    /**
     * TODO: test me!
     * Get an authentication from a given JWT token.
     *
     * @param token the token.
     * @return the authentication if the token is valid.
     * @throws InvalidTokenException if the token is not valid.
     */
    public Authentication getAuthentication(String token) throws InvalidTokenException {
        try {
            Claims claims = jwtParser.parseClaimsJwt(token).getBody();
            Set<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(JwtConstants.AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toUnmodifiableSet());
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), claims.get(JwtConstants.ID_KEY), authorities);
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.trace("Invalid JWT token");
            throw new InvalidTokenException();
        }
    }
}
