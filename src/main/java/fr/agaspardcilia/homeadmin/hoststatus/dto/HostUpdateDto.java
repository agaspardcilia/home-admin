package fr.agaspardcilia.homeadmin.hoststatus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.net.Inet4Address;
import java.util.UUID;

/**
 * TODO: comment me!
 */
public record HostUpdateDto(
        UUID id,
        @NotBlank
        String name,
        @NotNull
        Inet4Address address,
        boolean trackingEnabled
) {
}
