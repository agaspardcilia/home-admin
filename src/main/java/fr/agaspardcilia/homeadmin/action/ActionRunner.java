package fr.agaspardcilia.homeadmin.action;

import com.google.common.base.Preconditions;
import fr.agaspardcilia.homeadmin.action.dto.ActionExecutionDto;
import fr.agaspardcilia.homeadmin.action.dto.ActionExecutionOutcome;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO: test me!
 * TODO: check if it's a runnable
 * TODO: check the access rights
 * Run commands.
 */
@Log4j2
public class ActionRunner {
    private final String runnable;
    private final Path runnableDir;
    private final Duration timeout;

    /**
     * Constructor.
     *
     * @param runnable the name of the runnable within the runnable directory.
     * @param runnableDir the runnable directory path.
     * @param timeout the timeout of the runnable execution.
     */
    public ActionRunner(String runnable, Path runnableDir, Duration timeout) {
        this.runnable = Preconditions.checkNotNull(runnable);
        this.runnableDir = Preconditions.checkNotNull(runnableDir);
        this.timeout = timeout;
    }

    /**
     * Does the actual command running. This should be exception safe.
     *
     * @return the execution result as a {@link ActionExecutionDto}.
     */
    public ActionExecutionDto run() {
        try {
            Interpreter interpreter = getInterpreter();
            log.info("Running {} with {}", runnable, interpreter.getInterpreter());

            Process process = new ProcessBuilder()
                    .command(List.of(interpreter.getInterpreter(), getExecutable().toFile().getAbsolutePath()))
                    .start();

            boolean hasNotTimedOut = process.waitFor(timeout.getSeconds(), TimeUnit.SECONDS);
            Integer returnCode = hasNotTimedOut ? process.exitValue() : null;
            ActionExecutionOutcome outcome = getOutcome(returnCode);
            String stdin = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
            String stderr = IOUtils.toString(process.getErrorStream(), Charset.defaultCharset());
            return new ActionExecutionDto(outcome, returnCode, stdin, stderr, null);

        } catch (IOException e) {
            log.debug("Execution failed", e);
            return new ActionExecutionDto(ActionExecutionOutcome.EXCEPTION, 1, null, null, e.toString());
        } catch (InterruptedException e) {
            log.error("Interrupt!", e);
            Thread.currentThread().interrupt();
        }

        return new ActionExecutionDto(ActionExecutionOutcome.UNKNOWN, 1, null, null, null);
    }

    private ActionExecutionOutcome getOutcome(Integer returnCode) {
        if (returnCode == null) {
            return ActionExecutionOutcome.TIMEOUT;
        } else {
            return returnCode == 0 ? ActionExecutionOutcome.SUCCESS : ActionExecutionOutcome.FAILURE;
        }
    }

    private Path getExecutable() {
        return runnableDir.resolve(runnable);
    }

    private Interpreter getInterpreter() {
        return Arrays.stream(Interpreter.values())
                .filter(e -> runnable.endsWith(e.extension))
                .findFirst()
                .orElse(Interpreter.SH);
    }

    @AllArgsConstructor
    @Getter
    private enum Interpreter {
        SH(".sh", "sh"),
        PYTHON(".py", "python3");

        private final String extension;
        private final String interpreter;
    }
}
