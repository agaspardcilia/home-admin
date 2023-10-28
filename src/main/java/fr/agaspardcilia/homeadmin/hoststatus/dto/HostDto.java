package fr.agaspardcilia.homeadmin.hoststatus.dto;

import com.google.common.base.Preconditions;

import java.net.Inet4Address;
import java.time.Instant;
import java.util.UUID;

/**
 * TODO: comment me!
 */
public record HostDto (
        UUID id,
        String name,
        Inet4Address address,
        boolean trackingEnabled,
        Instant creationDate,
        Instant updateDate
) {
    public HostDto {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(address);
        Preconditions.checkNotNull(creationDate);
    }
}
