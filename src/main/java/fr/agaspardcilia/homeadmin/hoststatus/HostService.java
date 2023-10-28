package fr.agaspardcilia.homeadmin.hoststatus;

import fr.agaspardcilia.homeadmin.common.exception.UnknownEntityException;
import fr.agaspardcilia.homeadmin.hoststatus.dto.HostDto;
import fr.agaspardcilia.homeadmin.hoststatus.dto.HostUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * TODO: comment me!
 */
@Log4j2
@Service
@AllArgsConstructor
public class HostService {
    private final HostRepository repository;

    /**
     * TODO: comment me!
     * TODO: test me!
     * @param update
     * @return
     * @throws UnknownEntityException
     */
    public HostDto update(HostUpdateDto update) throws UnknownEntityException {
        Host toSave;
        if (update.id() != null) {
            toSave = repository.findById(update.id())
                    .map(h -> HostMapper.update(h, update))
                    .orElseThrow(() -> new UnknownEntityException("No host found with specified ID"));
        } else {
            toSave = HostMapper.fromDto(update);
        }

        return HostMapper.toDto(repository.save(toSave));
    }

    /**
     * TODO: comment me!
     * @param id
     */
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
