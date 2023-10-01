package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.authentication.dto.AuthenticationRequest;
import fr.agaspardcilia.homeadmin.authentication.exception.InvalidCredentialException;
import fr.agaspardcilia.homeadmin.authentication.exception.UserNotActivatedException;
import fr.agaspardcilia.homeadmin.user.UserDto;
import fr.agaspardcilia.homeadmin.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service handling JWT authentication.
 */
@Service
@AllArgsConstructor
public class JwtAuthenticationService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    /**
     * Authenticate with JWT. This will perform a credential check and generate a JWT token if it succeeds.
     *
     * @param request the authentication request.
     * @return the JWT.
     * @throws InvalidCredentialException when the provided credential are wrong.
     * @throws UserNotActivatedException when the user is not activated yet.
     */
    public String authenticateWithJwt(AuthenticationRequest request) throws InvalidCredentialException, UserNotActivatedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword())
        );
        UserDto user = userService.get(request.getMail())
                .orElseThrow(() -> new InvalidCredentialException("Wrong username/password"));
        if (user.isActive() == null || !user.isActive()) {
            throw new UserNotActivatedException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.createToken(user, authentication, request.isRememberMe());
    }
}
