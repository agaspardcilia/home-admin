package fr.agaspardcilia.homeadmin.action;

import com.google.common.base.Preconditions;
import fr.agaspardcilia.homeadmin.action.dto.ActionDto;
import fr.agaspardcilia.homeadmin.action.dto.ActionExecutionDto;
import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
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
    private static final Set<String> FILE_BLACK_LIST = Set.of(".DS_Store");
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
                "scriptDir is not a valid directory"
        );
    }


    /**
     * TODO: test me!
     * TODO: should be transactional
     * Scans the runnable directory for new actions and update the existence status of existing ones.
     *
     * @throws UnableToAccessPathException when the runnable directory is not accessible.
     */
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
     * TODO: test me!
     * @return all the actions in database.
     */
    public Set<ActionDto> getAll() {
        return repository.findAll().stream()
                .map(ActionMapper::toDto)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * TODO: test me!
     * TODO: should validate action name! (make sure it's unique)
     * TODO: add controller method.
     *
     * @param id the id of the action.
     * @param name the new name of the action.
     * @return the updated action.
     */
    public ActionDto renameAction(UUID id, String name) throws UnknownActionException {
        Action action = getEntity(id);
        action.setName(name);
        return ActionMapper.toDto(repository.save(action));
    }

    /**
     * Execute an action.
     *
     * @param id the ID of the action to execute.
     * @return the execution result.
     * @throws UnknownActionException of the action cannot be found in database.
     */
    public ActionExecutionDto run(UUID id) throws UnknownActionException {
        Action action = getEntity(id);
        ActionRunner runner = new ActionRunner(action.getRunnableFileName(), runnableDir, timeout);
        return runner.run();
    }

    private Action getEntity(UUID id) throws UnknownActionException {
        return repository.findById(id)
                .orElseThrow(() -> new UnknownActionException("Unable to find action"));
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
