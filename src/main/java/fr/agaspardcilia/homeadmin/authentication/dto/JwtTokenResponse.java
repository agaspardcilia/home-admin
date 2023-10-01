package fr.agaspardcilia.homeadmin.authentication.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The response of a successful authentication.
 *
 * @param idToken the JWT token.
 */
public record JwtTokenResponse(
    @JsonProperty("id_token") String idToken
) { }
