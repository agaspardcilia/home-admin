package fr.agaspardcilia.homeadmin.authentication;

import fr.agaspardcilia.homeadmin.authentication.dto.AuthenticationRequest;
import fr.agaspardcilia.homeadmin.authentication.dto.JwtTokenResponse;
import fr.agaspardcilia.homeadmin.authentication.exception.InvalidCredentialException;
import fr.agaspardcilia.homeadmin.authentication.exception.UserNotActivatedException;
import fr.agaspardcilia.homeadmin.common.annotation.PermitAll;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiForbiddenException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Authentication controller.
 */
@Controller
@AllArgsConstructor
public class UserJwtController {
    private JwtAuthenticationService authenticationService;

    /**
     * Performs an authentication request.
     *
     * @param authenticationRequest the credentials.
     * @return the JWT token if it succeeds.
     * @throws ApiForbiddenException if it fails.
     */
    @PostMapping("/authenticate")
    @PermitAll
    public ResponseEntity<JwtTokenResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String token = authenticationService.authenticateWithJwt(authenticationRequest);
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            return new ResponseEntity<>(new JwtTokenResponse(token), header, HttpStatus.OK);
        } catch (InvalidCredentialException | UserNotActivatedException e) {
            throw new ApiForbiddenException(e.getMessage());
        }
    }


}
