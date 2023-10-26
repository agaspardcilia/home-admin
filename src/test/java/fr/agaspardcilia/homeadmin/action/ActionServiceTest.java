package fr.agaspardcilia.homeadmin.action;

import fr.agaspardcilia.homeadmin.action.dto.ActionDto;
import fr.agaspardcilia.homeadmin.configuration.properties.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {
    @Mock
    private ActionRepository actionRepository;

    private AppProperties appProperties;
    private Path runnableDirectory;
    private ActionService actionService;
    @BeforeEach
    void setUp(@TempDir Path runnableDirectory) {
        this.runnableDirectory = runnableDirectory;
        this.appProperties = new AppProperties();
        fr.agaspardcilia.homeadmin.configuration.properties.Action action = new fr.agaspardcilia.homeadmin.configuration.properties.Action();
        action.setRunnableDir(runnableDirectory.toFile().getAbsolutePath());
        this.appProperties.setAction(action);
        this.actionService = new ActionService(appProperties, actionRepository);
    }

    @Test
    void testGetAll() {
        when(actionRepository.findAll()).thenReturn(List.of(action("foo"), action("bar")));
        assertEquals(Set.of(actionDto("foo"), actionDto("bar")), actionService.getAll());
    }

    @Test
    void testScanEmpty() throws UnableToAccessPathException {
        assertEquals(Set.of(), actionService.scanRunnableDirectory());
    }

    @Test
    void testScan() throws UnableToAccessPathException, IOException {
        when(actionRepository.saveAll(any())).then(invocation -> List.copyOf(invocation.getArgument(0)));
        File file = createFile("foo");
        assertEquals(Set.of(actionDto("foo")), actionService.scanRunnableDirectory());
        file.delete();
        assertEquals(Set.of(), actionService.scanRunnableDirectory());
    }

    private File createFile(String name) throws IOException {
        File result = runnableDirectory.resolve(name)
                .toFile();
        result.createNewFile();
        assertTrue(result.exists());
        return result;
    }

    private Action action(String name) {
        return Action.builder()
                .name(name)
                .runnableExists(true)
                .build();
    }

    private ActionDto actionDto(String name) {
        return ActionDto.builder()
                .name(name)
                .runnableExists(true)
                .build();
    }
}
