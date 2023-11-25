package fr.agaspardcilia.homeadmin.action;


import fr.agaspardcilia.homeadmin.action.dto.ActionDto;
import fr.agaspardcilia.homeadmin.action.dto.ActionExecutionDto;
import fr.agaspardcilia.homeadmin.action.exception.DuplicatedActionException;
import fr.agaspardcilia.homeadmin.action.exception.UnableToAccessPathException;
import fr.agaspardcilia.homeadmin.common.annotation.PermissionRequired;
import fr.agaspardcilia.homeadmin.common.exception.UnknownEntityException;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiBadRequestException;
import fr.agaspardcilia.homeadmin.common.exception.api.ApiInternalServerErrorException;
import fr.agaspardcilia.homeadmin.security.Permission;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/actions")
public class ActionController {
    private final ActionService service;

    @PermissionRequired(Permission.ACTIONS_SCAN_DIRECTORY)
    @PostMapping("/scan")
    public Set<ActionDto> scanRunnableDirector() {
        try {
            return service.scanRunnableDirectory();
        } catch (UnableToAccessPathException e) {
            throw new ApiInternalServerErrorException("Error while scanning directory, check your configuration");
        }
    }

    @PermissionRequired(Permission.ACTIONS_GET)
    @GetMapping
    public Set<ActionDto> getAll() {
        return service.getAll();
    }

    @PermissionRequired(Permission.ACTIONS_RUN)
    @PostMapping("/run/{id}")
    public ActionExecutionDto run(@PathVariable UUID id) throws UnknownEntityException {
        return service.run(id);
    }

    @PermissionRequired(Permission.ACTIONS_RENAME)
    @PostMapping("/rename/{id}")
    public ActionDto rename(@PathVariable UUID id, String newName) throws UnknownEntityException {
        try {
            return service.renameAction(id, newName);
        } catch (DuplicatedActionException e) {
            throw new ApiBadRequestException(e.getMessage());
        }
    }
}
