package fr.agaspardcilia.homeadmin.user;

import lombok.Data;

/**
 * A user creation request.
 */
@Data
public class UserCreationRequest {
    private String mail;
    private String password;
}
