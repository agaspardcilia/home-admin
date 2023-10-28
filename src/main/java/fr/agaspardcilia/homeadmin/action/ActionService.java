package fr.agaspardcilia.homeadmin.action;

import com.google.common.base.Preconditions;
import fr.agaspardcilia.homeadmin.action.dto.ActionDto;
import fr.agaspardcilia.homeadmin.action.dto.ActionExecutionDto;
import fr.agaspardcilia.homeadmin.action.exception.DuplicatedActionException;
import fr.agaspardcilia.homeadmin.action.exception.UnableToAccessPathException;
import fr.agaspardcilia.homeadmin.common.exception.UnknownEntityException;
import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Serves {@link Action}s.
 */
@Log4j2
@Service
public class ActionService {
    private static final Set<String> FILE_BLACK_LIST = Set.of(".DS_Store", ".gitignore", ".gitkeep");
    private final Path runnableDir;
    private final Duration timeout;
    private final ActionRepository repository;

    /**
     * Constructor.
     *
     * @param properties the {@link AppProperties}.
     * @param repository the {@link ActionRepository}.
     */
    public ActionService(AppProperties properties, ActionRepository repository) {
        this.repository = repository;
        this.runnableDir = Path.of(properties.getAction().getRunnableDir());
        this.timeout = properties.getAction().getExecutionTimeout();


        Preconditions.checkArgument(
                runnableDir.toFile().exists() && runnableDir.toFile().isDirectory(),
                "runnableDir is not a valid directory"
        );
    }


    /**
     * Scans the runnable directory for new actions and update the existence status of existing ones.
     *
     * @throws UnableToAccessPathException when the runnable directory is not accessible.
     */
    @Transactional
    public Set<ActionDto> scanRunnableDirectory() throws UnableToAccessPathException {
        try {
            // All entries in database.
            Map<String, Action> actionsByRunnableName = repository.findAll().stream()
                    .collect(Collectors.toMap(Action::getRunnableFileName, e -> e));

            // Fetch existing DB entry or create empty ones for newly found runnable.
            Map<String, Action> toSave = findAllInRunnableDir().stream()
                    .map(r -> actionsByRunnableName.getOrDefault(r, ActionFactory.getAction(r)))
                    .collect(Collectors.toMap(Action::getName, e -> e));

            // Disable all entries that are not in the runnable directory.
            actionsByRunnableName.entrySet().stream()
                    .filter(e -> !toSave.containsKey(e.getKey()))
                    .peek(e -> e.getValue().setRunnableExists(false))
                    .forEach(e -> toSave.put(e.getKey(), e.getValue()));

            return repository.saveAll(toSave.values()).stream()
                    .map(ActionMapper::toDto)
                    .collect(Collectors.toUnmodifiableSet());
        } catch (IOException e) {
            log.error("Scan error", e);
            throw new UnableToAccessPathException("Unable to access runnable directory");
        }
    }

    /**
     * @return all the actions in database.
     */
    public Set<ActionDto> getAll() {
        return repository.findAll().stream()
                .map(ActionMapper::toDto)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * TODO: comment me! with exceptions
     * TODO: test me!
     *
     * @param id the id of the action.
     * @param name the new name of the action.
     * @return the updated action.
     */
    public ActionDto renameAction(UUID id,
                                  @Pattern(message = "Alphanumeric only", regexp = "\\w+") String name
    ) throws UnknownEntityException, DuplicatedActionException {
        Action action = getEntity(id);
        if (repository.existsByIdNotAndName(action.getId(), name)) {
            throw new DuplicatedActionException("Name already in use by another action");
        }

        action.setName(name);
        return ActionMapper.toDto(repository.save(action));
    }

    /**
     * Execute an action.
     *
     * @param id the ID of the action to execute.
     * @return the execution result.
     * @throws UnknownEntityException of the action cannot be found in database.
     */
    public ActionExecutionDto run(UUID id) throws UnknownEntityException {
        Action action = getEntity(id);
        ActionRunner runner = new ActionRunner(action.getRunnableFileName(), runnableDir, timeout);
        return runner.run();
    }

    private Action getEntity(UUID id) throws UnknownEntityException {
        return repository.findById(id)
                .orElseThrow(() -> new UnknownEntityException("Unable to find action with specified ID"));
    }

    private Set<String> findAllInRunnableDir() throws IOException {
        try (Stream<Path> stream = Files.list(runnableDir)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(e -> !FILE_BLACK_LIST.contains(e)) // Eliminate black listed files.
                    .collect(Collectors.toUnmodifiableSet());
        }
    }
}
