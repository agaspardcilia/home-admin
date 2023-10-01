package fr.agaspardcilia.homeadmin.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * An authentication request.
 */
@Data
public class AuthenticationRequest {
    @NotBlank
    @Email
    @Size(max = 128)
    private String mail;
    @NotBlank
    @Size(max = 64)
    private String password;
    private boolean rememberMe = false;
}
