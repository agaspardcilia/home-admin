package fr.agaspardcilia.homeadmin.hoststatus;

import com.google.common.base.Preconditions;
import fr.agaspardcilia.homeadmin.hoststatus.dto.HostDto;
import fr.agaspardcilia.homeadmin.hoststatus.dto.HostUpdateDto;

/**
 * TODO: commment me!
 */
public class HostMapper {
    private HostMapper() {
        // Do not instantiate! >:(
    }

    /**
     * TODO: comment me!
     * @param host
     * @return
     */
    public static HostDto toDto(Host host) {
        return new HostDto(
                host.getId(),
                host.getName(),
                host.getAddress(),
                host.getTrackingEnabled(),
                host.getCreationDate(),
                host.getUpdateDate()
        );
    }

    /**
     * TODO: comment me!
     * TODO: test me!
     * @param dto
     * @return
     */
    public static Host fromDto(HostUpdateDto dto) {
        Preconditions.checkArgument(dto.id() == null);
        return Host.builder()
                .name(dto.name())
                .address(dto.address())
                .trackingEnabled(dto.trackingEnabled())
                .build();
    }

    /**
     * TODO: comment me!
     * @param toUpdate
     * @param update
     * @return
     */
    public static Host update(Host toUpdate, HostUpdateDto update) {
        Preconditions.checkNotNull(toUpdate.getId());
        Preconditions.checkNotNull(update.id());
        Preconditions.checkArgument(toUpdate.getId().equals(update.id()));

        toUpdate.setName(update.name());
        toUpdate.setAddress(update.address());
        toUpdate.setTrackingEnabled(update.trackingEnabled());
        return toUpdate;
    }
}
